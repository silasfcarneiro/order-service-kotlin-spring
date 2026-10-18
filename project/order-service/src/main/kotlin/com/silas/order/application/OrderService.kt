package com.silas.order.application

import com.silas.common.domain.Money
import com.silas.common.domain.OrderId
import com.silas.order.application.command.OrderCreateCommand
import com.silas.order.application.port.InventoryClient
import com.silas.order.application.port.OrderRepository
import com.silas.order.application.port.PaymentClient
import com.silas.order.application.port.PriceCatalog
import com.silas.order.application.query.OrderView
import com.silas.order.application.query.toView
import com.silas.order.domain.Order
import com.silas.order.domain.OrderItem
import com.silas.order.domain.exception.OrderNotFoundException
import com.silas.order.domain.exception.PaymentUnavailableException
import com.silas.order.domain.exception.ProductUnavailableException
import com.silas.order.domain.exception.StockUnavailableException
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val priceCatalog: PriceCatalog,
    private val inventoryClient: InventoryClient,
    private val paymentClient: PaymentClient
) {

    fun createOrder(command: OrderCreateCommand): Order {
        val order = orderRepository.save(buildOrder(command))   // PENDING
        return orderRepository.save(runSaga(order))             // CONFIRMED ou REJECTED
    }

    fun getOrder(orderId: OrderId): OrderView =
        orderRepository.findByOrderId(orderId)?.toView()
            ?: throw OrderNotFoundException(orderId)

    fun getAll(): List<OrderView> = orderRepository.findAll().map { it.toView() }

    // Monta o pedido com os preços do catálogo (o que você já tinha)
    private fun buildOrder(command: OrderCreateCommand): Order {
        val products = priceCatalog
            .findProducts(command.items.map { it.sku })
            .associateBy { it.sku }

        val unavailable = command.items
            .map { it.sku }
            .filter { products[it]?.active != true }
        if (unavailable.isNotEmpty()) throw ProductUnavailableException(unavailable)

        return Order(
            orderId = OrderId.new(),
            items = command.items.map { item ->
                OrderItem(
                    sku = item.sku,
                    unitPrice = Money.of(products.getValue(item.sku).price),
                    quantity = item.quantity
                )
            }
        )
    }

    // Saga orquestrada: reserva → cobra → confirma, com compensação
    private fun runSaga(order: Order): Order {
        val id = order.orderId.value.toString()

        // 1. reservar o estoque
        try {
            inventoryClient.reserve(id, order.items)
        } catch (ex: StockUnavailableException) {
            return order.reject("Sem estoque para um ou mais itens.")   // nada a compensar
        }

        // 2. cobrar
        val payment = try {
            paymentClient.charge(id, order.total)
        } catch (ex: PaymentUnavailableException) {
            inventoryClient.release(id)                                  // compensação
            return order.reject("Pagamento indisponível no momento.")
        }

        if (!payment.approved) {
            inventoryClient.release(id)                                  // compensação
            return order.reject(payment.reason ?: "Pagamento recusado.")
        }

        // 3. confirmar o estoque e o pedido
        inventoryClient.confirm(id)
        return order.confirm()
    }
}
