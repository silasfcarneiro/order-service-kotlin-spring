package com.silas.payment.application.command

import com.silas.common.domain.Money

class CreatePaymentCommand (
    val orderId: String,
    val amount: Money,
)
