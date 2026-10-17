package com.silas.order.adapters.`in`.web

import com.silas.common.domain.OrderId
import com.silas.order.adapters.`in`.web.dto.*
import com.silas.order.application.OrderService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@RequestMapping("/v1/api/orders")
class OrderController(private val service: OrderService) {

    @PostMapping
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest)
        : ResponseEntity<CreateOrderResponse> {
        val result = service.createOrder(request.toCommand())
        return ResponseEntity.created(URI("v1/api/orders/${result.orderId}"))
            .body(result.toResponse())
    }

    @GetMapping("{orderId}")
    fun getOrder(@PathVariable orderId: OrderId): ResponseEntity<OrderResponse> {
        val order = service.getOrder(orderId)

        return ResponseEntity.ok(order.toResponse())
    }

    @GetMapping
    fun getOrders(): ResponseEntity<List<OrderResponse>> {
        val orders = service.getAll()
        return ResponseEntity.ok(orders.map { it.toResponse() })
    }
}
