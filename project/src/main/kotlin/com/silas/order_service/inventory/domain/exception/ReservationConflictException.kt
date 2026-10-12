package com.silas.order_service.inventory.domain.exception

import com.silas.order_service.orders.domain.OrderId

class ReservationConflictException(
    val sku: String,
    orderId: OrderId,
    existingQuantity: Int,
    quantity: Int
) : IllegalStateException(
    "Reserva em conflito para $sku: pedido $orderId, disponível $existingQuantity, desejada: $quantity."
)
