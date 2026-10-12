package com.silas.order_service.shared.domain

inline fun ensure(condition: Boolean, error: () -> Exception) {
    if (!condition) throw error()
}
