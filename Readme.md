## Exchange Application

### Overview

This application demonstrates how to implement Hexagonal Architecture (also known as Ports and Adapters Architecture) in a Spring Boot application. The primary goal of Hexagonal Architecture is to create loosely coupled application components that can be easily tested and maintained.

### Hexagonal Architecture

Hexagonal Architecture divides the application into three main parts:

1. **Core (Domain)**: Contains the business logic and domain entities. It is independent of any external systems or frameworks.
2. **Ports**: Interfaces that define the communication between the core and the outside world. Ports can be inbound (driven) or outbound (driving).
3. **Adapters**: Implementations of the ports. Adapters can be primary (driven) or secondary (driving).

### Project Structure

The project is organized into the following packages:

- `application`: Contains the application domain, services and use cases.
- `adapter`: Contains the implementations of the ports (e.g., REST controllers, database repositories).
- `common`: Contains common configurations and utilities.

### Technologies Used

- **Spring Boot**: For building the application.
- **Spring Data JPA**: For database interactions.
- **H2 Database**: In-memory database for testing.
- **WireMock**: For mocking external HTTP services in tests.
- **Spock**: For writing tests in Groovy.

### Endpoints

The application exposes the following endpoint:

- `/account/{id:(1|2|3|4)}/balance?currency=(USD|EUR)`: Retrieves the account balance in the specified currency.

### Running the Application

To run the application, use the following command:

```bash
./gradlew bootRun
```

### Testing

To run the tests, use the following command:

```bash
./gradlew test
```

### Example Code

#### Port Layer

```java
package com.bartoszjaszczak.exchange.application.port.out

import com.bartoszjaszczak.exchange.application.domain.Balance

interface BalanceProvider {
    fun get(accountId: Long): Balance?
}
```

#### Domain Layer

```java
package com.bartoszjaszczak.exchange.application.service;

import com.bartoszjaszczak.exchange.domain.exchange.ExchangeFacade;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
    private final ExchangeFacade exchangeFacade;

    public ExchangeService(ExchangeFacade exchangeFacade) {
        this.exchangeFacade = exchangeFacade;
    }

    public MonetaryAmount getBalance(Long accountId, Currency currency) {
        // Business logic to get balance
    }
}
```

#### Adapter Layer

```java
package com.bartoszjaszczak.exchange.adapter.in.web;

import com.bartoszjaszczak.exchange.application.service.ExchangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/account/{id}/balance")
    public MonetaryAmount getBalance(@PathVariable Long id, @RequestParam Currency currency) {
        return exchangeService.getBalance(id, currency);
    }
}
```

### Conclusion

This application serves as an example of how to implement Hexagonal Architecture in a Spring Boot application.