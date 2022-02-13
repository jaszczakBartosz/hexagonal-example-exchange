package com.milionpugs.exchange.domain.balance

import org.javamoney.moneta.Money

interface BalanceProvider {
    fun get(accountId: Long): Balance?
}

data class Balance(val balance: Money)

