package com.milionpugs.exchange.domain.exchange

import com.milionpugs.exchange.domain.rates.RatesNotAvailable
import com.milionpugs.exchange.domain.rates.RatesProvider
import org.javamoney.moneta.Money
import org.javamoney.moneta.function.MonetaryOperators.rounding
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import javax.money.CurrencyUnit

@Service
class ExchangeFacade(private val ratesProvider: RatesProvider) {
    operator fun invoke(balance: Money, currency: CurrencyUnit): Money = ratesProvider.getRate(currency)?.let {
        balance.exchange(it, currency)
    } ?: throw RatesNotAvailable()
}

fun Money.exchange(rate: BigDecimal, currency: CurrencyUnit): Money =
    divide(rate)
        .factory
        .setCurrency(currency)
        .create()
        .with(rounding(HALF_EVEN, currency.defaultFractionDigits))