package com.silas.order.adapters.out.http.payment.dto

data class PaymentRequest(val orderId: String, val amount: String)

data class PaymentResponse(val orderId: String, val status: String, val reason: String?)

data class PaymentResult(val approved: Boolean, val reason: String?)

fun PaymentResponse.toPaymentResult() = PaymentResult(
    approved = status == "APPROVED",
    reason = reason
)
