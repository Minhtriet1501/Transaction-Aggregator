# Transaction Aggregator

A Spring Boot service that pulls a user's transactions from two separate
backend servers, combines them, and returns a single sorted list.

I built this to get hands-on with the kind of problems that come up when one
service has to talk to several others: flaky upstreams, slow responses, and
repeated requests for the same data.

## What it does

When a client asks for an account's transactions, the service calls both
backends at the same time, waits for both to come back, then merges the results
and sorts them newest-first before sending them on.

A few things it handles along the way:

- **Concurrent calls.** The two backends are queried in parallel with
  `CompletableFuture`, so total wait time is roughly the slower of the two
  rather than both added together.
- **Flaky servers.** The backends sometimes return 503 or 529 instead of data.
  The service retries up to 5 times before giving up on that server.
- **Repeated lookups.** Results are cached per account with Spring's
  `@Cacheable`, so asking for the same account again skips the network round trip.

## Endpoint
