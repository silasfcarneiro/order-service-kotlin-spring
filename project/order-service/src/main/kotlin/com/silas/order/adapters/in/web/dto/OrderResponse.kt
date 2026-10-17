package com.silas.order.adapters.`in`.web.dto

import com.silas.order.application.query.OrderView

class OrderResponse(
    val orderId: String,
    val items: List<OrderItemResponse>,
    val total: String
) {
    class OrderItemResponse(
        val sku: String,
        val price: String,
        val quantity: Int,
        val subtotal: String
    )
}

fun OrderView.toResponse(): OrderResponse {
    return OrderResponse(
        orderId = this.orderId,
        total = this.total.toString(),
        items = this.items.map {
            OrderResponse.OrderItemResponse(
                sku = it.sku,
                price = it.unitPrice.toString(),
                quantity = it.quantity,
                subtotal = it.subtotal.toString()
            )
        },
    )
}
