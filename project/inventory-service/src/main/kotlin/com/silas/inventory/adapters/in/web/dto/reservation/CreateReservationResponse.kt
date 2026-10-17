package com.silas.inventory.adapters.`in`.web.dto.reservation

import com.silas.inventory.domain.Reservation

class CreateReservationResponse(val reservationId: String)

fun Reservation.toCreateResponse(): CreateReservationResponse {
    return CreateReservationResponse(
        reservationId = this.reservationId.value.toString()
    )
}
