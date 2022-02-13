package com.milionpugs.exchange.inrastructure.h2db

import com.milionpugs.exchange.domain.balance.Balance
import com.milionpugs.exchange.domain.balance.BalanceProvider
import org.javamoney.moneta.Money
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary

@Repository
class H2BalanceProvider(private val dsl: DSLContext) : BalanceProvider {

    override fun get(accountId: Long): Balance? =
        dsl.select(field(BALANCE), field(CURRENCY))
            .from(table(ACCOUNT))
            .where(field(ID).eq(accountId))
            .fetchOne()
            ?.map {
                Balance(Money.of(it.getBalance(), it.getCurrency()))
            }

    companion object {
        const val BALANCE = "balance"
        const val ID = "id"
        const val ACCOUNT = "account"
        const val CURRENCY = "currency"
    }

    private fun Record.getBalance(): BigDecimal =
        get(BALANCE, Double::class.java)?.let { BigDecimal.valueOf(it) } ?: throw IllegalStateException()

    private fun Record.getCurrency(): CurrencyUnit =
        get(CURRENCY, String::class.java)?.let { Monetary.getCurrency(it) } ?: throw IllegalStateException()
}