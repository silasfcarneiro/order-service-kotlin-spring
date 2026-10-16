package com.silas.order.adapters.out.persistence

import com.silas.common.domain.OrderId
import com.silas.order.application.port.OrderRepository
import com.silas.order.domain.Order
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryOrderRepository : OrderRepository {

    private val orderList = ConcurrentHashMap<OrderId, Order>()

    override fun save(order: Order): Order {
        orderList[order.orderId] = order
        return order
    }

    override fun findByOrderId(orderId: OrderId): Order? = orderList[orderId]

    override fun findAll(): List<Order> =
        orderList.values.toList()
}
