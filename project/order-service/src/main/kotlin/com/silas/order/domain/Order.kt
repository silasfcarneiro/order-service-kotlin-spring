package com.silas.order.domain

import com.silas.common.domain.Money
import com.silas.common.domain.OrderId

class Order(
    val orderId: OrderId,
    items: List<OrderItem>,
    val status: OrderStatus = OrderStatus.PENDING,
    val rejectionReason: String? = null,
) {
    val items: List<OrderItem> = items.toList()

    init {
        require(this.items.isNotEmpty()) { "Pedido sem itens" }
        require((status == OrderStatus.REJECTED) == (rejectionReason != null)) {
            "Motivo de recusa só existe em pedido recusado."
        }
    }

    val total: Money
        get() = items.fold(Money.ZERO) { acc, item ->
            acc + item.unitPrice * item.quantity
        }

    fun confirm(): Order = when (status) {
        OrderStatus.PENDING -> withStatus(OrderStatus.CONFIRMED)
        OrderStatus.CONFIRMED -> this
        OrderStatus.REJECTED -> throw IllegalStateException("Pedido $orderId foi recusado e não pode ser confirmado.")
    }

    fun reject(reason: String): Order {
        require(reason.isNotBlank()) { "Informe o motivo da recusa." }
        return when (status) {
            OrderStatus.PENDING -> withStatus(OrderStatus.REJECTED, reason)
            OrderStatus.REJECTED -> this
            OrderStatus.CONFIRMED -> throw IllegalStateException("Pedido $orderId já foi confirmado e não pode ser recusado.")
        }
    }

    private fun withStatus(newStatus: OrderStatus, reason: String? = null) =
        Order(orderId, items, newStatus, reason)
}

enum class OrderStatus { PENDING, CONFIRMED, REJECTED }
