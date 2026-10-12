package com.silas.order_service.inventory.domain

import com.silas.order_service.inventory.domain.enums.ReservationStatus
import com.silas.order_service.orders.domain.OrderId

data class Reservation(
    val orderId: OrderId,
    val sku: String,
    val quantity: Int,
    val status: ReservationStatus,
)
{
    init {
        require(quantity > 0) { "A quantidade reservada tem que ser maior que zero." }
    }

    /** Pagamento aprovado: o produto foi vendido. */
    fun confirm(): Reservation = when (status) {
        ReservationStatus.RESERVED  -> copy(status = ReservationStatus.CONFIRMED)  // a única mudança permitida
        ReservationStatus.CONFIRMED -> this                                        // repetição: nada muda
        ReservationStatus.RELEASED  -> throw IllegalStateException(
            "Não é possível confirmar a reserva do pedido $orderId: ela já foi devolvida."
        )
    }

    /** Pagamento falhou: a reserva é desfeita (compensação da Saga). */
    fun release(): Reservation = when (status) {
        ReservationStatus.RESERVED  -> copy(status = ReservationStatus.RELEASED)   // a única mudança permitida
        ReservationStatus.RELEASED  -> this                                        // repetição: nada muda
        ReservationStatus.CONFIRMED -> throw IllegalStateException(
            "Não é possível devolver a reserva do pedido $orderId: o produto já foi vendido."
        )
    }

    /** Mesma chave com dados diferentes não é repetição: é conflito. */
    fun matches(sku: String, quantity: Int): Boolean =
        this.sku == sku && this.quantity == quantity
}
