package com.silas.order.adapters.out.http.catalog

import com.silas.order.adapters.out.http.catalog.dto.CatalogProductResponse
import com.silas.order.adapters.out.http.catalog.dto.CatalogProductResult
import com.silas.order.adapters.out.http.catalog.dto.toCatalogProduct
import com.silas.order.application.port.PriceCatalog
import com.silas.order.domain.exception.CatalogUnavailableException
import org.apache.kafka.shaded.com.google.protobuf.LazyStringArrayList.emptyList
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class HttpPriceCatalog(
    @Qualifier("catalogRestClient") private val restClient: RestClient
) : PriceCatalog {

    @Retryable(maxRetries = 2, delay = 200, multiplier = 2.0, includes = [CatalogUnavailableException::class])
    override fun findProducts(skus: List<String>): List<CatalogProductResult> {
        try {
            val response = restClient.get()
                .uri {
                    it.path("/v1/api/products")
                        .queryParam("skus", skus.joinToString(","))
                        .build()
                }
                .retrieve()
                .body<List<CatalogProductResponse>>()
            return  response?.map { it.toCatalogProduct() } ?: emptyList<CatalogProductResult>()
        } catch (e: ResourceAccessException) {
            throw CatalogUnavailableException(e)
        }
    }
}
