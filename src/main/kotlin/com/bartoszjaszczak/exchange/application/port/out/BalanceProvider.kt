package com.bartoszjaszczak.exchange.application.port.out

import com.bartoszjaszczak.exchange.application.domain.Balance

interface BalanceProvider {
    fun get(accountId: Long): Balance?
}

