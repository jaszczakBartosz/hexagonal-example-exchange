package com.bartoszjaszczak.exchange.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfig(@Value("\${rate.url}") private val baseUrl: String) {
    @Bean
    fun rateRestTemplate(): RestTemplate = RestTemplateBuilder().rootUri(baseUrl).build()
}