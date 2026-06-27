# Transaction Aggregator

A Spring Boot service that aggregates transaction data from multiple remote servers.

## Features

- Fetches transaction data from two remote servers concurrently using async requests
- Merges and sorts transactions by timestamp (newest first)
- Handles server errors (503, 529) with up to 5 retries
- Caches results per account to optimize performance

## Endpoints

```
GET /aggregate?account={accountNumber}
```

Returns a JSON array of transactions sorted by timestamp descending.

## Running the app

```bash
./gradlew :app:bootRun
```
