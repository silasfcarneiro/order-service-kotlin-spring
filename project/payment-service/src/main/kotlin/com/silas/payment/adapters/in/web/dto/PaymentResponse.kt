package com.silas.payment.adapters.`in`.web.dto

import com.silas.payment.domain.Payment
import com.silas.payment.domain.enums.PaymentStatus

class PaymentResponse (
    val paymentId: String,
    val orderId: String,
    val amount: String,
    val status: PaymentStatus
)

fun Payment.toResponse(): PaymentResponse {
    return PaymentResponse(
        paymentId = this.paymentId.id.toString(),
        orderId = this.orderId,
        amount = this.amount.toString(),
        status = this.status
    )
}
