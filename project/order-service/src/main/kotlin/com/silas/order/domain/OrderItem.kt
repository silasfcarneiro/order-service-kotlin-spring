package com.silas.order.domain

import com.silas.common.domain.Money

class OrderItem(
    val sku: String,
    val unitPrice: Money,
    val quantity: Int,
) {

    init {
        require(quantity > 0) { "Item de pedido com quantidade inválida." }
        require(unitPrice >= Money.ZERO) { "Item de pedido com valor inválido." }
    }

    fun subtotal(): Money {
        return unitPrice * quantity
    }
}
