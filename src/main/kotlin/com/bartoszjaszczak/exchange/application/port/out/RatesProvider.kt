package com.bartoszjaszczak.exchange.application.port.out

import java.math.BigDecimal
import javax.money.CurrencyUnit

interface RatesProvider {
    fun getRate(currency: CurrencyUnit): BigDecimal?
}