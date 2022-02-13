package com.milionpugs.exchange.domain.rates

import java.math.BigDecimal
import javax.money.CurrencyUnit

interface RatesProvider {
    fun getRate(currency: CurrencyUnit): BigDecimal?
}

class RatesNotAvailable : RuntimeException("Rates are not available now")