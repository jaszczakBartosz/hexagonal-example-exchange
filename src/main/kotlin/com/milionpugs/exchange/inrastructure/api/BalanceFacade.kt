package com.milionpugs.exchange.inrastructure.api

import com.milionpugs.exchange.domain.balance.AccountBalanceGetter
import com.milionpugs.exchange.domain.balance.AccountBalanceRequest
import org.javamoney.moneta.Money
import org.springframework.stereotype.Service
import javax.money.Monetary.getCurrency

@Service
class BalanceFacade(private val accountBalanceGetter: AccountBalanceGetter) {

    fun get(accountId: Long, currency: String) =
        accountBalanceGetter.get(AccountBalanceRequest(accountId, getCurrency(currency))).toAccountBalanceDto()
}

fun Money.toAccountBalanceDto() = AccountBalanceDto(numberStripped.toPlainString(), currency.currencyCode)