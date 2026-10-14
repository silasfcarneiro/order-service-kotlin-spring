package com.silas.common.domain

inline fun ensure(condition: Boolean, error: () -> Exception) {
    if (!condition) throw error()
}
