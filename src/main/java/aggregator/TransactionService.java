package aggregator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Service
class TransactionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public TransactionService() {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatusCode statusCode) {
                return false; // không throw, để mình tự check status code
            }
        });
    }
    @Cacheable("transactions")
    public Transaction[] getTransaction(String url) {
        for(int i = 0; i < 5; i++) {
            ResponseEntity<Transaction[]> f = restTemplate.getForEntity(url, Transaction[].class);
            int status = f.getStatusCode().value();
            if(status != 503 && status != 529) {
                return f.getBody();
            }
        }
        return new Transaction[0];
    }

}

