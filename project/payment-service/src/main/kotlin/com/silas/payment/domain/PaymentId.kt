package com.silas.payment.domain

import java.util.UUID

@JvmInline
value class PaymentId(val id: UUID) {

    companion object {
        fun new() = PaymentId(UUID.randomUUID())
    }
}
