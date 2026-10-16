package com.silas.order.application

import com.silas.common.domain.Money
import com.silas.common.domain.OrderId
import com.silas.order.application.command.OrderCreateCommand
import com.silas.order.application.port.OrderRepository
import com.silas.order.application.port.PriceCatalog
import com.silas.order.application.query.OrderView
import com.silas.order.application.query.toView
import com.silas.order.domain.Order
import com.silas.order.domain.OrderItem
import com.silas.order.domain.exception.OrderNotFoundException
import com.silas.order.domain.exception.ProductUnavailableException
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val priceCatalog: PriceCatalog
) {

    fun createOrder(command: OrderCreateCommand): Order {
        val products = priceCatalog
            .findProducts(command.items.map { it.sku })
            .associateBy { it.sku }

        val unavailable = command.items
            .map { it.sku }
            .filter { products[it]?.active != true }
        if (unavailable.isNotEmpty()) throw ProductUnavailableException(unavailable)

        val order = Order(
            orderId = OrderId.new(),
            items = command.items.map { item ->
                OrderItem(
                    sku = item.sku,
                    unitPrice = Money.of(products.getValue(item.sku).price),
                    quantity = item.quantity
                )
            }
        )
        return orderRepository.save(order)
    }

    fun getOrder(orderId: OrderId): OrderView =
        orderRepository.findByOrderId(orderId)?.toView()
            ?: throw OrderNotFoundException(orderId)

    fun getAll(): List<OrderView> = orderRepository.findAll().map { it.toView() }
}
