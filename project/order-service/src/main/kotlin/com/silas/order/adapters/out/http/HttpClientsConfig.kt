package com.silas.order.adapters.out.http

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class HttpClientsConfig {

    @Bean("catalogRestClient")
    fun catalogRestClient(@Value("\${catalog.base-url}") baseUrl: String): RestClient =
        RestClient.builder().baseUrl(baseUrl).build()

    @Bean("paymentRestClient")
    fun paymentClient(@Value("\${payment.base-url}") baseUrl: String): RestClient =
        RestClient.builder().baseUrl(baseUrl).build()

    @Bean("inventoryRestClient")
    fun inventoryRestClient(@Value("\${inventory.base-url}") baseUrl: String): RestClient =
        RestClient.builder().baseUrl(baseUrl).build()
}
