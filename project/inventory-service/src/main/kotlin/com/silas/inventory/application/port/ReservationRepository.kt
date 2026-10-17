package com.silas.inventory.application.port

import com.silas.inventory.domain.Reservation
import com.silas.inventory.domain.ReservationId

interface ReservationRepository {
    fun saveAll(params: List<Reservation>): List<Reservation>
    fun findById(reservationId: ReservationId): Reservation?
    fun findByOrderId(orderId: String): List<Reservation>
}
