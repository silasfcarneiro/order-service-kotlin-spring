package com.silas.inventory.adapters.`in`.web.dto.reservation

import com.silas.inventory.domain.Reservation
import com.silas.inventory.domain.enums.ReservationStatus

class ReleaseReservationResponse(val reservationId: String, val status: ReservationStatus)


fun Reservation.toReleaseResponse(): ReleaseReservationResponse {
    return ReleaseReservationResponse(
        reservationId = this.reservationId.toString(),
        status = this.status
    )
}
