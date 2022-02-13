package com.milionpugs.exchange.inrastructure.api

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Pattern

@Validated
@RestController
@RequestMapping("account")
class BalanceEndpoint(private val balanceFacade: BalanceFacade) {

    @GetMapping("{id}/balance")
    fun getBalance(
        @PathVariable("id") id: Long,
        @Pattern(regexp = "^(USD|EUR)$") @RequestParam(value = "currency") currency: String
    ) = balanceFacade.get(id, currency)
}

data class AccountBalanceDto(val balance: String, val currency: String)