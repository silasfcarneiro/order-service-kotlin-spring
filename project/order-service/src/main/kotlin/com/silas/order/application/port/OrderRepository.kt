package com.silas.order.application.port

import com.silas.common.domain.OrderId
import com.silas.order.domain.Order

interface OrderRepository {
    fun save(order: Order): Order
    fun findByOrderId(orderId: OrderId): Order?
    fun findAll(): List<Order>
}
