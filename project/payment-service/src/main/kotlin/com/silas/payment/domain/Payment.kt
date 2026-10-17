package com.silas.payment.domain

import com.silas.common.domain.Money
import com.silas.payment.domain.enums.PaymentStatus

data class Payment (
    val paymentId: PaymentId,
    val orderId: String,
    val amount: Money,
    val status: PaymentStatus
) {

    init {
        require(amount > Money.ZERO) { "O valor do pagamento tem que ser maior que zero." }
    }
}
