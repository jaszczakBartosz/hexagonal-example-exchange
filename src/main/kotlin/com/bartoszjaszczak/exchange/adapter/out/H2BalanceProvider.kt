package com.bartoszjaszczak.exchange.adapter.out

import com.bartoszjaszczak.exchange.application.domain.Balance
import com.bartoszjaszczak.exchange.application.port.out.BalanceProvider
import org.javamoney.moneta.Money.of
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.math.BigDecimal.valueOf
import javax.money.CurrencyUnit
import javax.money.Monetary.getCurrency

@Repository
class H2BalanceProvider(private val dsl: DSLContext) : BalanceProvider {

    override fun get(accountId: Long): Balance? =
        dsl.select(field(BALANCE), field(CURRENCY))
            .from(table(ACCOUNT))
            .where(field(ID).eq(accountId))
            .fetchOne()
            ?.map {
                Balance(of(it.getBalance(), it.getCurrency()))
            }

    companion object {
        private const val BALANCE = "balance"
        private const val ID = "id"
        private const val ACCOUNT = "account"
        private const val CURRENCY = "currency"
    }

    private fun Record.getBalance(): BigDecimal =
        get(BALANCE, Double::class.java)?.let { valueOf(it) } ?: throw IllegalStateException()

    private fun Record.getCurrency(): CurrencyUnit =
        get(CURRENCY, String::class.java)?.let { getCurrency(it) } ?: throw IllegalStateException()
}