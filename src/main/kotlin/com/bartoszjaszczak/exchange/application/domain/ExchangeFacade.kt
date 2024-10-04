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

class RatesNotAvailable : RuntimeException("Rates are not available now")