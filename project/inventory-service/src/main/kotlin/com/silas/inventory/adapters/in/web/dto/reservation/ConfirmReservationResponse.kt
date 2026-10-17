package com.silas.inventory.adapters.`in`.web.dto.reservation

import com.silas.inventory.domain.Reservation

class ConfirmReservationResponse(val reservationId: String)

fun Reservation.toConfirmResponse(): ConfirmReservationResponse {
    return ConfirmReservationResponse(
        reservationId = this.reservationId.toString()
    )
}
