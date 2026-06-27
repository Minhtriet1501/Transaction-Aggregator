package aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
@EnableCaching

public class Application {

    private final TransactionService service;

    public Application(TransactionService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }


    @GetMapping("/aggregate")
    public List<Transaction> aggregate (@RequestParam String account) throws Exception {

        String url1 = "http://localhost:8888/transactions?account=" + account;
        String url2 = "http://localhost:8889/transactions?account=" + account;

        CompletableFuture<Transaction[]> f1 = CompletableFuture.supplyAsync(() -> service.getTransaction(url1));
        CompletableFuture<Transaction[]> f2 = CompletableFuture.supplyAsync(() -> service.getTransaction(url2));



        List<Transaction> result = new ArrayList<Transaction>();
        result.addAll(List.of(f1.get()));
        result.addAll(List.of(f2.get()));

        result.sort(Comparator.comparing(Transaction::getTimestamp).reversed());

        return result;

    }
}




