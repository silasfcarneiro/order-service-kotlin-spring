package com.silas.order.adapters.out.http.inventory.dto

class ReserveRequest (val orderId: String, val items: List<Item>) {
    data class Item(val sku: String, val quantity: Int)
}
