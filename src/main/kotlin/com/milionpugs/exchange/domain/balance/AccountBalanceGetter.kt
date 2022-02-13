package com.milionpugs.exchange.domain.balance

import com.milionpugs.exchange.domain.exchange.ExchangeFacade
import org.javamoney.moneta.Money
import org.springframework.stereotype.Service
import javax.money.CurrencyUnit


interface AccountBalanceGetter {
    fun get(accountBalanceRequest: AccountBalanceRequest): Money
}

@Service
class AccountBalanceGetterAdapter(
    private val exchange: ExchangeFacade,
    private val balanceProvider: BalanceProvider
) : AccountBalanceGetter {

    override fun get(accountBalanceRequest: AccountBalanceRequest): Money = with(accountBalanceRequest) {
        balanceProvider.get(accountId)?.let { exchange(it.balance, targetCurrency) }
    } ?: throw AccountNotFound(accountBalanceRequest.accountId)
}

data class AccountBalanceRequest(val accountId: Long, val targetCurrency: CurrencyUnit)

class AccountNotFound(id: Long) : RuntimeException("Account $id not found")
