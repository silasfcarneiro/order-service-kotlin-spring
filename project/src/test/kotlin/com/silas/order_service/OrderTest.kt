package com.silas.order_service

import com.silas.order_service.orders.domain.Order
import com.silas.order_service.orders.domain.OrderId
import com.silas.order_service.orders.domain.OrderItem
import com.silas.order_service.shared.domain.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrderTest {

    @Test
    fun `testa pedidos sem itens`() {
        assertFailsWith<IllegalArgumentException> { Order(OrderId.new(), items = listOf()) }
    }

    @Test
    fun `testa valor total do pedido`() {
        val order = Order(
            OrderId.new(), items = listOf(
                OrderItem(
                    sku = "test",
                    quantity = 2,
                    unitPrice = Money.of("10")
                ),
                OrderItem(
                    sku = "test1",
                    quantity = 2,
                    unitPrice = Money.of("40")
                ),
                OrderItem(
                    sku = "test2",
                    quantity = 1,
                    unitPrice = Money.of("30")
                ),
                OrderItem(
                    sku = "test3",
                    quantity = 5,
                    unitPrice = Money.of("20")
                )
            )
        )

        assertEquals(Money.of("230"), order.total)
    }

    @Test
    fun `alterar a lista original nao altera o pedido`() {
        // dado: uma lista que pode mudar, guardada numa variável
        val itensOriginais = mutableListOf(
            OrderItem(sku = "test", quantity = 2, unitPrice = Money.of("10"))
        )
        val order = Order(OrderId.new(), items = itensOriginais)

        // quando: alguém mexe na lista original depois de criar o pedido
        itensOriginais.clear()

        // então: o pedido continua igual a quando foi criado
        assertEquals(1, order.items.size)
        assertEquals(Money.of("20"), order.total)
    }

    @Test
    fun `testa valor item do pedido negativo`() {
        assertFailsWith<IllegalArgumentException> {
            Order(
                OrderId.new(), items = listOf(
                    OrderItem(
                        sku = "test",
                        quantity = 1,
                        unitPrice = Money.of("-10")
                    )
                )
            )
        }
    }

    @Test
    fun `testa quantidade item do pedido maior que zero`() {
        assertFailsWith<IllegalArgumentException> {
            Order(
                OrderId.new(), items = listOf(
                    OrderItem(
                        sku = "test",
                        quantity = 0,
                        unitPrice = Money.of("10")
                    )
                )
            )
        }
    }

    @Test
    fun `testa quantidade item do pedido negativo`() {
        assertFailsWith<IllegalArgumentException> {
            Order(
                OrderId.new(), items = listOf(
                    OrderItem(
                        sku = "test",
                        quantity = -1,
                        unitPrice = Money.of("10")
                    )
                )
            )
        }
    }

    @Test
    fun `testa valor do item zerado`() {
        val order = Order(
            OrderId.new(), items = listOf(
                OrderItem(
                    sku = "test",
                    quantity = 1,
                    unitPrice = Money.of("0")
                )
            )
        )

        assertEquals(Money.of("0"), order.total)
    }
}
