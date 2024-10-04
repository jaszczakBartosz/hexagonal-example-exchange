package com.bartoszjaszczak.exchange.application.domain

import com.bartoszjaszczak.exchange.application.port.`in`.AccountBalanceGetter
import com.bartoszjaszczak.exchange.application.port.out.BalanceProvider
import org.javamoney.moneta.Money
import org.springframework.stereotype.Service
import javax.money.CurrencyUnit


@Service
class AccountBalanceGetterService(
    private val exchange: ExchangeFacade,
    private val balanceProvider: BalanceProvider
) : AccountBalanceGetter {

    override fun get(accountBalanceRequest: AccountBalanceRequest): Money = with(accountBalanceRequest) {
        balanceProvider
            .get(accountId)
            ?.let {
                exchange(it.balance, targetCurrency)
            }
    } ?: throw AccountNotFound(accountBalanceRequest.accountId)
}

data class AccountBalanceRequest(val accountId: Long, val targetCurrency: CurrencyUnit)

class AccountNotFound(id: Long) : RuntimeException("Account $id not found")

data class Balance(val balance: Money)
