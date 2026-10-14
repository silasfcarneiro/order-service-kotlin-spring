package com.silas.common.domain

import java.util.*

@JvmInline
value class OrderId private constructor(val value: UUID) {

    companion object {
        fun new() = OrderId(UUID.randomUUID())
    }
}
