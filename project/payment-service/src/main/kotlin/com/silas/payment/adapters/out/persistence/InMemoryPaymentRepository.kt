package com.silas.payment.adapters.out.persistence

import com.silas.payment.application.port.PaymentRepository
import com.silas.payment.domain.Payment
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPaymentRepository : PaymentRepository {

    private val payments = ConcurrentHashMap<String, Payment>()

    override fun findByOrderId(orderId: String): Payment? = payments[orderId]

    override fun save(payment: Payment): Payment {
        payments[payment.orderId] = payment
        return payment
    }
}
