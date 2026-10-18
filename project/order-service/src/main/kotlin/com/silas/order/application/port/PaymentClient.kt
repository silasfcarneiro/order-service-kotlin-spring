package com.silas.order.application.port

import com.silas.common.domain.Money
import com.silas.order.adapters.out.http.payment.dto.PaymentResult

interface PaymentClient {
    fun charge(orderId: String, amount: Money): PaymentResult
}
