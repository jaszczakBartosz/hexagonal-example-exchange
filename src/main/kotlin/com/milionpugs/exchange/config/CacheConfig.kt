package com.milionpugs.exchange.config

import com.milionpugs.exchange.inrastructure.rates.NbpRateApiClient.Companion.RATE_CACHE
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableCaching
@EnableScheduling
class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "rate", name = ["cache"], havingValue = "true")
    fun cacheManager(): CacheManager = ConcurrentMapCacheManager(RATE_CACHE)

    @Bean
    @ConditionalOnProperty(prefix = "rate", name = ["cache"], havingValue = "false")
    fun noOpCacheManager(): CacheManager = NoOpCacheManager()

    // Normalnie użyłbym jakiegoś providera do cache który ma wbudowany ttl
    @CacheEvict(allEntries = true, cacheNames = [RATE_CACHE])
    @Scheduled(cron = "0 0 6 * * ?")
    fun cacheEvict() {
    }
}