package com.bartoszjaszczak.exchange.application.port.`in`

import com.bartoszjaszczak.exchange.application.domain.AccountBalanceRequest
import org.javamoney.moneta.Money

interface AccountBalanceGetter {
    fun get(accountBalanceRequest: AccountBalanceRequest): Money
}