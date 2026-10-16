package com.silas.order.adapters.out.catalog

import com.silas.order.application.port.PriceCatalog
import com.silas.order.domain.exception.CatalogUnavailableException
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class HttpPriceCatalog(private val restClient: RestClient) : PriceCatalog {

    @Retryable(maxRetries = 2, delay = 200, multiplier = 2.0, includes = [ResourceAccessException::class])
    override fun findProducts(skus: List<String>): List<CatalogProductResponse> {
        try {
            val response = restClient.get()
                .uri {
                    it.path("/v1/api/products")
                        .queryParam("skus", skus.joinToString(","))
                        .build()
                }
                .retrieve()
                .body<List<CatalogProductResponse>>()

            return response?.map { it.toCatalogProduct() } ?: emptyList()
        } catch (e: ResourceAccessException) {
            throw CatalogUnavailableException(e)
        }
    }
}
