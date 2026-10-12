package com.silas.order_service.shared.domain.exceptions

class InvalidMoneyException(val input: String, reason: String) :
    IllegalArgumentException("Valor monetário inválido \"$input\": $reason")
