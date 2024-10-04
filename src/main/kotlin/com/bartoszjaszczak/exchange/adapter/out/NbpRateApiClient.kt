package com.bartoszjaszczak.exchange.adapter.out

import com.bartoszjaszczak.exchange.application.port.out.RatesProvider
import org.springframework.cache.annotation.Cacheable
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import javax.money.CurrencyUnit

@Component
class NbpRateApiClient(private val rateRestTemplate: RestTemplate, private val retryTemplate: RetryTemplate) :
    RatesProvider {

    @Cacheable(RATE_CACHE)
    override fun getRate(currency: CurrencyUnit): BigDecimal = retryTemplate.execute<BigDecimal, RuntimeException> {
        with(currency) {
            rateRestTemplate.getForObject(
                "/api/exchangerates/rates/a/$currencyCode",
                RateResponse::class.java
            )
                ?.let { it.rates.firstOrNull()?.mid }
        }
    }

    companion object {
        const val RATE_CACHE = "RATE_CACHE"
    }
}


data class RateResponse(val rates: List<RateValue>)
data class RateValue(val mid: BigDecimal)