package com.silas.order.application.query

import com.silas.common.domain.Money
import com.silas.order.domain.Order
import com.silas.order.domain.OrderStatus

data class OrderView(
    val orderId: String,
    val status: OrderStatus,
    val rejectionReason: String?,
    val items: List<Item>,
    val total: Money
) {
    data class Item(
        val sku: String,
        val quantity: Int,
        val unitPrice: Money,
        val subtotal: Money
    )
}

fun Order.toView() = OrderView(
    orderId = orderId.value.toString(),
    status = status,
    rejectionReason = rejectionReason,
    items = items.map {
        OrderView.Item(
            sku = it.sku,
            quantity = it.quantity,
            unitPrice = it.unitPrice,
            subtotal = it.subtotal()
        )
    },
    total = total
)
