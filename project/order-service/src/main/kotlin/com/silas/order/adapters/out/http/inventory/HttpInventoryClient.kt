package com.silas.order.adapters.out.http.inventory

import com.silas.order.adapters.out.http.inventory.dto.ReserveRequest
import com.silas.order.application.port.InventoryClient
import com.silas.order.domain.OrderItem
import com.silas.order.domain.exception.InventoryUnavailableException
import com.silas.order.domain.exception.StockUnavailableException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient

@Component
class HttpInventoryClient(
    @Qualifier("inventoryRestClient") private val restClient: RestClient
) : InventoryClient {

    @Retryable(maxRetries = 2, delay = 200, multiplier = 2.0, includes = [InventoryUnavailableException::class])
    override fun reserve(orderId: String, items: List<OrderItem>) = call {
        restClient.post()
            .uri("/v1/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReserveRequest(orderId, items.map { ReserveRequest.Item(it.sku, it.quantity) }))
            .retrieve()
            .onStatus({ it.value() == 422 }) { _, _ -> throw StockUnavailableException(orderId) }
            .toBodilessEntity()
    }

    @Retryable(maxRetries = 2, delay = 200, multiplier = 2.0, includes = [InventoryUnavailableException::class])
    override fun confirm(orderId: String) = call {
        restClient.post()
            .uri("/v1/api/reservations/{orderId}/confirm", orderId)
            .retrieve()
            .toBodilessEntity()
    }

    @Retryable(maxRetries = 2, delay = 200, multiplier = 2.0, includes = [InventoryUnavailableException::class])
    override fun release(orderId: String) = call {
        restClient.post()
            .uri("/v1/api/reservations/{orderId}/release", orderId)
            .retrieve()
            .toBodilessEntity()
    }

    private fun call(request: () -> Unit) {
        try {
            request()
        } catch (ex: ResourceAccessException) {
            throw InventoryUnavailableException(ex)
        }
    }
}
