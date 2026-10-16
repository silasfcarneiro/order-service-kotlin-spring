package com.silas.order.adapters.out.catalog

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class CatalogClientConfig {
    @Bean
    fun catalogRestClient(@Value("\${catalog.base-url}") baseUrl: String): RestClient =
        RestClient.builder().baseUrl(baseUrl).build()
}
