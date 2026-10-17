package com.silas.inventory.domain

import java.util.*

@JvmInline
value class ReservationId(val value: UUID) {
    companion object {
        fun new() = ReservationId(UUID.randomUUID())
    }
}
