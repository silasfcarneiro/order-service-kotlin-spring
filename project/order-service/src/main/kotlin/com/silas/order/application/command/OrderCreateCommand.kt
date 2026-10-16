package com.silas.order.application.command

data class OrderCreateCommand(
    val items: List<Item>
) {
    data class Item(val sku: String, val quantity: Int)
}
