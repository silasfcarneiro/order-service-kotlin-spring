package com.silas.inventory.adapters.`in`.web.dto.reservation

import com.silas.inventory.application.command.ReservationCreateCommand

class CreateReservationRequest(
    val orderId: String,
    val items: List<Item>
) {
    data class Item(
        val sku: String,
        val quantity: Int
    )
}


fun CreateReservationRequest.toCommand(): ReservationCreateCommand {
    return ReservationCreateCommand(
        orderId = orderId,
        items = items.map {
            ReservationCreateCommand.Item(
                sku = it.sku,
                quantity = it.quantity
            )
        }
    )
}
