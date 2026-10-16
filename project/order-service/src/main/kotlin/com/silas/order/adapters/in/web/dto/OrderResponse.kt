package com.silas.order.adapters.`in`.web.dto

import com.silas.common.domain.Money
import com.silas.order.application.query.OrderView

class OrderResponse (
    val orderId: String,
    val items: List<OrderItemResponse>,
    val total: Money
) {
    class OrderItemResponse(
        val sku: String,
        val price: Money,
        val quantity: Int,
        val subtotal: Money
    )
}

fun OrderView.toDto(): OrderResponse {
    return OrderResponse(
        orderId = this.orderId,
        total = this.total,
        items = this.items.map {
            OrderResponse.OrderItemResponse(
                sku = it.sku,
                price = it.unitPrice,
                quantity = it.quantity,
                subtotal = it.subtotal
            )
        },
    )
}
