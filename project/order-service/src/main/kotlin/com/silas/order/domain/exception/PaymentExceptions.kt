package com.silas.order.domain.exception

class PaymentConflictException: IllegalArgumentException()

class PaymentUnavailableException(ex: Throwable?):
    IllegalArgumentException(ex?.message ?: "Payment not available")
