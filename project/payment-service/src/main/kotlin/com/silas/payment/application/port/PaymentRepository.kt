package com.silas.payment.application.port

import com.silas.payment.domain.Payment

interface PaymentRepository {
    fun findByOrderId(orderId: String): Payment?
    fun save(payment: Payment): Payment
}
