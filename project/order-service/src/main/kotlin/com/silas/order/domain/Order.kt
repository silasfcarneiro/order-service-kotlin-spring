package com.silas.order.domain

import com.silas.common.domain.Money
import com.silas.common.domain.OrderId

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
