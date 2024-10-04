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

```kotlin
package com.bartoszjaszczak.exchange.application.port.out

import com.bartoszjaszczak.exchange.application.domain.Balance

interface BalanceProvider {
    fun get(accountId: Long): Balance?
}
```

#### Domain Layer

```kotlin
package com.bartoszjaszczak.exchange.application.domain

import com.bartoszjaszczak.exchange.application.port.out.RatesProvider
import org.javamoney.moneta.Money
import org.javamoney.moneta.function.MonetaryOperators.rounding
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import javax.money.CurrencyUnit

@Service
class ExchangeFacade(private val ratesProvider: RatesProvider) {
    operator fun invoke(balance: Money, currency: CurrencyUnit): Money =
            ratesProvider
                    .getRate(currency)
            ?.let {
        balance.exchangeToCurrency(it, currency)
    } ?: throw RatesNotAvailable()

    private fun Money.exchangeToCurrency(rate: BigDecimal, currency: CurrencyUnit): Money =
    divide(rate)
            .factory
                    .setCurrency(currency)
            .create()
            .with(rounding(HALF_EVEN, currency.defaultFractionDigits))
}
```

#### Adapter Layer

```kotlin
package com.bartoszjaszczak.exchange.adapter.`in`

import com.bartoszjaszczak.exchange.application.domain.AccountBalanceRequest
import com.bartoszjaszczak.exchange.application.port.`in`.AccountBalanceGetter
import jakarta.validation.constraints.Pattern
import org.javamoney.moneta.Money
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.money.Monetary.getCurrency

@Validated
@RestController
@RequestMapping("account")
class BalanceEndpoint(private val accountBalanceGetter: AccountBalanceGetter) {

    @GetMapping("{id}/balance")
    fun getBalance(
            @PathVariable("id") id: Long,
            @Pattern(regexp = "^(USD|EUR)$") @RequestParam(value = "currency") currency: String
    ) = accountBalanceGetter
            .get(AccountBalanceRequest(id, getCurrency(currency)))
            .toAccountBalanceDto()
}
```

### Conclusion

This application serves as an example of how to implement Hexagonal Architecture in a Spring Boot application.