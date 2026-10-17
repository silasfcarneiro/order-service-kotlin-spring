package com.silas.payment.adapters.`in`.web.dto

import com.silas.payment.domain.Payment

class CreatePaymentResponse (
    val id: String,
)

fun Payment.toCreateResponse(): CreatePaymentResponse {
    return CreatePaymentResponse(
        id = this.paymentId.id.toString()
    )
}
