package com.bartoszjaszczak.exchange.domain.exchange

import com.bartoszjaszczak.exchange.application.domain.ExchangeFacade
import com.bartoszjaszczak.exchange.application.port.out.RatesProvider
import spock.lang.Shared
import spock.lang.Specification

import javax.money.Monetary

import static org.javamoney.moneta.Money.of

class ExchangeFacadeTest extends Specification {

    private def rateProvider = Mock(RatesProvider.class)
    private def exchange = new ExchangeFacade(rateProvider)
    @Shared
    private final def USD = Monetary.getCurrency("USD")
    @Shared
    private final def PLN = Monetary.getCurrency("PLN")

    def "should return #resultValue when rate is #rate for balance #balance"() {
        given:
        rateProvider.getRate(USD) >> BigDecimal.valueOf(rate)

        when:
        def result = exchange.invoke(balance, USD)

        then:
        result == resultValue

        where:
        balance        | rate | resultValue
        of(30.00, PLN) | 3    | of(10, USD)
        of(0.00, PLN)  | 3    | of(0, USD)
        of(35.00, PLN) | 3.5  | of(10, USD)
        of(10.00, PLN) | 3    | of(3.33, USD)
        of(10.00, PLN) | 3.1  | of(3.23, USD)
        of(1000, PLN)  | 2.67 | of(374.53, USD)
    }
}