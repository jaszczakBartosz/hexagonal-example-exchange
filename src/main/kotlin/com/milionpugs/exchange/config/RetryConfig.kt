package com.milionpugs.exchange.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.remoting.RemoteAccessException
import org.springframework.retry.support.RetryTemplate


@Configuration
class RetryConfig {

    @Bean
    fun retryTemplate(): RetryTemplate = RetryTemplate.builder()
        .maxAttempts(3)
        .exponentialBackoff(100, 2.0, 2000, true)
        .retryOn(RemoteAccessException::class.java)
        .build()
}