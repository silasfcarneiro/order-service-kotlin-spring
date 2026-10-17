package com.silas.inventory.domain

import com.silas.common.domain.OrderId
import com.silas.inventory.domain.enums.ReservationStatus
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ReservationTest {

    @Test
    fun `valida quantidade reservada maior que zero`() {
        assertFailsWith<IllegalArgumentException> {
            Reservation(
                orderId = OrderId.new(),
                sku = "teste",
                quantity = 0,
                status = ReservationStatus.RELEASED
            )
        }
    }

    @Test
    fun `valida confirmar reserva com status reservado`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.RESERVED
        )
        val result = reservation.confirm()
        assertEquals(ReservationStatus.CONFIRMED, result.status)
    }

    @Test
    fun `valida confirmar reserva com status confirmado restorna ele mesmo`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.CONFIRMED
        )
        val result = reservation.confirm()
        assertEquals(reservation, result)
    }

    @Test
    fun `valida confirmar reserva com status devolvido`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.RELEASED
        )

        assertFailsWith<IllegalStateException> {
            reservation.confirm()
        }
    }

    @Test
    fun `valida a devolucao com o status reservado`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.RESERVED
        )
        val result = reservation.release()
        assertEquals(ReservationStatus.RELEASED, result.status)
    }

    @Test
    fun `valida a devolucao com o status devolvido`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.RELEASED
        )
        val result = reservation.release()
        assertEquals(reservation, result)
    }

    @Test
    fun `valida a devolucao com o status confirmado`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.CONFIRMED
        )

        assertFailsWith<IllegalStateException> {
            reservation.release()
        }
    }

    @Test
    fun `valida o matches com os valores iguais`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.CONFIRMED
        )

        assertTrue { reservation.matches("teste", 2) }
    }

    @Test
    fun `valida o matches com os valores diferentes`() {
        val reservation = Reservation(
            orderId = OrderId.new(),
            sku = "teste",
            quantity = 2,
            status = ReservationStatus.CONFIRMED
        )

        assertFalse { reservation.matches("Teste", 1) }
        assertFalse { reservation.matches("teste", 3) }
        assertFalse { reservation.matches("teste2", 2) }
    }
}
