package com.silas.inventory.adapters.`in`.web

import com.silas.inventory.adapters.`in`.web.dto.reservation.*
import com.silas.inventory.application.ReservationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/v1/api/reservations")
class ReservationController(
    private val service: ReservationService
) {

    @PostMapping
    fun reservation(@Valid @RequestBody body: CreateReservationRequest)
        : ResponseEntity<List<CreateReservationResponse>> {
        val reservation = service.create(body.toCommand())

        val idsRoute = reservation.joinToString(separator = ",") { it.reservationId.value.toString() }

        return ResponseEntity
            .created(URI.create("/reservations/$idsRoute"))
            .body(reservation.map { it.toCreateResponse() })
    }

    @PostMapping("{orderId}/confirm")
    fun confirmReservation(@PathVariable orderId: String)
    : ResponseEntity<List<ConfirmReservationResponse>> {
        val response = service.confirm(orderId)
        return ResponseEntity
            .ok(response.map { it.toConfirmResponse() })
    }

    @PostMapping("{orderId}/release")
    fun release(@PathVariable orderId: String)
    : ResponseEntity<List<ReleaseReservationResponse>> {
        val response = service.release(orderId)
        return ResponseEntity
            .ok(response.map { it.toReleaseResponse() })
    }
}
