package com.silas.order.domain.exception

import org.springframework.web.client.ResourceAccessException

class PaymentConflictException: IllegalArgumentException()

class PaymentUnavailableException(ex: ResourceAccessException?):
    IllegalArgumentException(ex?.message ?: "Payment not available")
