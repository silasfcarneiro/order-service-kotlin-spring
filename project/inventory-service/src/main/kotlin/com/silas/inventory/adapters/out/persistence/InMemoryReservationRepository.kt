package com.silas.inventory.adapters.out.persistence

import com.silas.inventory.application.port.ReservationRepository
import com.silas.inventory.domain.Reservation
import com.silas.inventory.domain.ReservationId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryReservationRepository : ReservationRepository {

    private val reservations = ConcurrentHashMap<ReservationId, Reservation>()

    override fun saveAll(params: List<Reservation>): List<Reservation> {
        params.forEach { reservations[it.reservationId] = it }
        return params
    }

    override fun findById(reservationId: ReservationId): Reservation? =
        reservations[reservationId]

    override fun findByOrderId(orderId: String): List<Reservation> =
        reservations.values.filter { it.orderId == orderId }
}
