package com.silas.order_service.orders.domain

import java.util.*

@JvmInline
value class OrderId private constructor(val value: UUID) {

    companion object {
        fun new() = OrderId(UUID.randomUUID())
    }
}
