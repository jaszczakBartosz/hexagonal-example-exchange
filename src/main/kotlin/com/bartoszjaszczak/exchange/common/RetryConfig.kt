package com.bartoszjaszczak.exchange.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


@Configuration
class RetryConfig {

    @Bean
    fun retryTemplate(): RetryTemplate = RetryTemplate.builder()
        .maxAttempts(3)
        .exponentialBackoff(100, 2.0, 2000, true)
        .retryOn(HttpServerErrorException::class.java)
        .retryOn(SocketTimeoutException::class.java)
        .retryOn(TimeoutException::class.java)
        .retryOn(HttpClientErrorException.TooManyRequests::class.java)
        .build()
}