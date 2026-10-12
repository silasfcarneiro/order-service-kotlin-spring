package com.silas.order_service.orders.domain

import com.silas.order_service.shared.domain.Money

class OrderItem(
    val sku: String,
    val unitPrice: Money,
    val quantity: Int,
) {

    init {
        require(quantity > 0) { "Item de pedido com quantidade inválida." }
        require(unitPrice >= Money.ZERO) { "Item de pedido com valor inválido." }
    }
}
