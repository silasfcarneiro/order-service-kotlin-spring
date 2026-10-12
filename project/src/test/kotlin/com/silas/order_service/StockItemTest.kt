package com.silas.order_service

import com.silas.order_service.inventory.domain.StockItem
import com.silas.order_service.inventory.domain.exception.InsufficientStockException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class StockItemTest {

    @Test
    fun `testa iniciando um estoque disponivel menor que zero`() {
        assertFailsWith<IllegalArgumentException> {
            StockItem(
                sku = "test",
                available = -1,
                reserved = 1
            )
        }
    }

    @Test
    fun `testa iniciando um estoque reservado menor que zero`() {
        assertFailsWith<IllegalArgumentException> {
            StockItem(
                sku = "test",
                available = 0,
                reserved = -1
            )
        }
    }

    @Test
    fun `testa iniciando um estoque disponivel com 10`() {
        val stock = StockItem(
            sku = "test",
            available = 10,
            reserved = 0
        )
        assertEquals(10, stock.available)
    }

    @Test
    fun `testa iniciando um estoque reservado com 10`() {
        val stock = StockItem(
            sku = "test",
            available = 0,
            reserved = 10
        )
        assertEquals(10, stock.reserved)
    }

    @Test
    fun `testa iniciando reserva de estoque com 10 unidades`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        val reserved = stock.reserve(10)

        assertEquals(10, reserved.reserved)
        assertEquals(90, reserved.available)
    }

    @Test
    fun `testa iniciando reserva de estoque com quantidade negativa`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        assertFailsWith<IllegalArgumentException> {
            stock.reserve(-1)
        }
    }

    @Test
    fun `testa iniciando reserva de estoque com quantidade zerada`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        assertFailsWith<IllegalArgumentException> {
            stock.reserve(0)
        }
    }

    @Test
    fun `testa iniciando reserva de estoque com quantidade disponivel menor que a quantidade desejada`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        assertFailsWith<InsufficientStockException> {
            stock.reserve(101)
        }
    }

    @Test
    fun `testa iniciando devolucao de estoque com 10 unidades`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 10
        )
        val reserved = stock.release(10)

        assertEquals(0, reserved.reserved)
        assertEquals(110, reserved.available)
    }

    @Test
    fun `testa iniciando devolucao de estoque com quantidade zerada`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        assertFailsWith<IllegalArgumentException> {
            stock.release(0)
        }
    }

    @Test
    fun `testa iniciando devolucao de estoque onde a reserva é menor que a quantidade desejada`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 0
        )
        assertFailsWith<IllegalStateException> {
            stock.release(1)
        }
    }

    @Test
    fun `testa iniciando confirmacao de reserva com 10 unidades`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 10
        )
        val reserved = stock.confirm(10)

        assertEquals(0, reserved.reserved)
        assertEquals(100, reserved.available)
    }

    @Test
    fun `testa iniciando uma confirmacao com quantidade negativa`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 10
        )
        assertFailsWith<IllegalArgumentException> {
            stock.confirm(0)
        }
    }

    @Test
    fun `testa iniciando uma confirmacao com quantidade reservada menor que a quantidade desejada`() {
        val stock = StockItem(
            sku = "test",
            available = 100,
            reserved = 10
        )
        assertFailsWith<IllegalStateException> {
            stock.confirm(11)
        }
    }
}
