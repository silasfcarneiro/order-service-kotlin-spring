package com.silas.payment.application.port

import com.silas.common.domain.Money
import com.silas.payment.adapters.out.gateway.GatewayResult

interface PaymentGateway {
    fun charge(orderId: String, amount: Money): GatewayResult
}
