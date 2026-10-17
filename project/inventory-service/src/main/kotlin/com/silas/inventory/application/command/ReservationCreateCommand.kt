package com.silas.inventory.application.command

class ReservationCreateCommand(
    val orderId: String,
    val items: List<Item>
) {
    data class Item(
        val sku: String,
        val quantity: Int
    )
}
