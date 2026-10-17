package com.silas.payment.adapters.out.gateway

import com.silas.payment.domain.enums.PaymentStatus

data class GatewayResult (
    val reason: String?,
    val status: PaymentStatus
)
