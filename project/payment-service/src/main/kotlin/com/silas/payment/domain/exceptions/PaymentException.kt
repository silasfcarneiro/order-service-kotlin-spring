package com.silas.payment.domain.exceptions

class PaymentConflictException(orderId: String):
    IllegalArgumentException("Já existe um pagamento para este pedido: $orderId")
