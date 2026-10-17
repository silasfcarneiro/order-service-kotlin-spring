package com.silas.payment.adapters.out.gateway

import com.silas.common.domain.Money
import com.silas.payment.application.port.PaymentGateway
import com.silas.payment.domain.enums.PaymentStatus
import org.springframework.stereotype.Component

@Component
class FakePaymentGateway : PaymentGateway {

    override fun charge(orderId: String, amount: Money): GatewayResult {
        val gatewayStatus = if (amount > LIMIT) GatewayStatus.DECLINED else GatewayStatus.AUTHORIZED
        return GatewayResult(status = gatewayStatus.toDomain(), reason =  gatewayStatus.reason)
    }

    // os status "do fornecedor": ninguém fora deste arquivo conhece
    private enum class GatewayStatus(val reason: String?) {
        AUTHORIZED(null),
        DECLINED("Valor acima do limite."),
        INSUFFICIENT_FUNDS("Saldo insuficiente.");

        fun toDomain(): PaymentStatus = when (this) {
            AUTHORIZED -> PaymentStatus.APPROVED
            DECLINED, INSUFFICIENT_FUNDS -> PaymentStatus.REJECTED
        }
    }

    companion object {
        private val LIMIT = Money.of("5000")
    }
}
