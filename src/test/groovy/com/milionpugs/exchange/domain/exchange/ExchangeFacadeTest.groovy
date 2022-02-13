package com.milionpugs.exchange.domain.exchange

import com.milionpugs.exchange.domain.rates.RatesProvider
import spock.lang.Shared
import spock.lang.Specification

import javax.money.Monetary

import static org.javamoney.moneta.Money.of

class ExchangeFacadeTest extends Specification {

    def rateProvider = Mock(RatesProvider.class)
    def exchange = new ExchangeFacade(rateProvider)
    @Shared
    def USD = Monetary.getCurrency("USD")
    @Shared
    def PLN = Monetary.getCurrency("PLN")

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