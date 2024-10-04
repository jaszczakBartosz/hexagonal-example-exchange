package com.bartoszjaszczak.exchange.adapter.`in`

import com.bartoszjaszczak.exchange.application.domain.AccountBalanceRequest
import com.bartoszjaszczak.exchange.application.port.`in`.AccountBalanceGetter
import jakarta.validation.constraints.Pattern
import org.javamoney.moneta.Money
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.money.Monetary.getCurrency

@Validated
@RestController
@RequestMapping("account")
class BalanceEndpoint(private val accountBalanceGetter: AccountBalanceGetter) {

    @GetMapping("{id}/balance")
    fun getBalance(
        @PathVariable("id") id: Long,
        @Pattern(regexp = "^(USD|EUR)$") @RequestParam(value = "currency") currency: String
    ) = accountBalanceGetter
        .get(AccountBalanceRequest(id, getCurrency(currency)))
        .toAccountBalanceDto()
}

data class AccountBalanceDto(val balance: String, val currency: String)

fun Money.toAccountBalanceDto() = AccountBalanceDto(numberStripped.toPlainString(), currency.currencyCode)