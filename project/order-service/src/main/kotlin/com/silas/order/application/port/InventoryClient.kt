package com.silas.order.application.port

import com.silas.order.domain.OrderItem

interface InventoryClient {
    fun reserve(orderId: String, items: List<OrderItem>)
    fun confirm(orderId: String)
    fun release(orderId: String)
}
