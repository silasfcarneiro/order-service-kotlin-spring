package com.silas.payment.application

import com.silas.payment.application.command.CreatePaymentCommand
import com.silas.payment.application.port.PaymentGateway
import com.silas.payment.application.port.PaymentRepository
import com.silas.payment.domain.Payment
import com.silas.payment.domain.PaymentId
import com.silas.payment.domain.exceptions.PaymentConflictException
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val repository: PaymentRepository,
    private val gatewayPayment: PaymentGateway
) {

    fun create(command: CreatePaymentCommand): Payment {
        repository.findByOrderId(command.orderId)?.let { existing ->
            if (existing.amount != command.amount) throw PaymentConflictException(command.orderId)
            return existing
        }

        val result = gatewayPayment.charge(command.orderId, command.amount)

        return repository.save(
            Payment(
                paymentId = PaymentId.new(),
                orderId = command.orderId,
                amount = command.amount,
                status = result.status
            )
        )
    }

    fun getByOrderId(orderId: String): Payment? =
        repository.findByOrderId(orderId)
}
