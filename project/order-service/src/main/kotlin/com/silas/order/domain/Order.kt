package com.silas.order.domain

import com.silas.common.domain.OrderId
import com.silas.common.domain.Money

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
