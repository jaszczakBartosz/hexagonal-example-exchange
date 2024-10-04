package com.bartoszjaszczak.exchange.common

import com.bartoszjaszczak.exchange.adapter.out.NbpRateApiClient.Companion.RATE_CACHE
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit


@Configuration
@EnableCaching
@EnableScheduling
class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "rate", name = ["cache"], havingValue = "true")
    fun cacheManager(): CacheManager = CaffeineCacheManager().also {
        it.setCaffeine(Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS))
    }

    @Bean
    @ConditionalOnProperty(prefix = "rate", name = ["cache"], havingValue = "false")
    fun noOpCacheManager(): CacheManager = NoOpCacheManager()

    @CacheEvict(allEntries = true, cacheNames = [RATE_CACHE])
    @Scheduled(cron = "0 0 6 * * ?")
    fun cacheEvict() {
    }
}