package com.silas.order_service.orders.domain

import com.silas.order_service.shared.domain.Money

class Order(
    val orderId: OrderId,
    items: List<OrderItem>,
) {
    val items: List<OrderItem> = items.toList()

    init {
        require(items.isNotEmpty()) { "Pedido sem itens" }
    }

    val total: Money
        get() = items.fold(Money.ZERO) { acc, item ->
            acc + item.unitPrice * item.quantity
        }
}
