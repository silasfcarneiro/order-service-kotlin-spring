package com.silas.payment.adapters.`in`.web.dto

import com.silas.common.domain.Money
import com.silas.payment.application.command.CreatePaymentCommand

class CreatePaymentRequest (
    val orderId: String,
    val amount: String,
)

fun CreatePaymentRequest.toCommand(): CreatePaymentCommand {
    return CreatePaymentCommand(
        orderId = this.orderId,
        amount = Money.of(this.amount),
    )
}
