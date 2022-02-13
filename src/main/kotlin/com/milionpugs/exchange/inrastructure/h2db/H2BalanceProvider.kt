package com.milionpugs.exchange.inrastructure.h2db

import com.milionpugs.exchange.domain.balance.Balance
import com.milionpugs.exchange.domain.balance.BalanceProvider
import org.javamoney.moneta.Money
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary

@Repository
class H2BalanceProvider(private val dsl: DSLContext) : BalanceProvider {

    override fun get(accountId: Long): Balance? =
        dsl.select(field(BALANCE))
            .from(table(ACCOUNT))
            .where(field(ID).eq(accountId))
            .fetchOne()
            ?.map { it.into(BigDecimal::class.java) }
            ?.let {
                Balance(Money.of(it, PLN))
            }

    companion object {
        const val BALANCE = "balance"
        const val ID = "id"
        const val ACCOUNT = "account"
        val PLN: CurrencyUnit = Monetary.getCurrency("PLN")
    }
}