package com.silas.common.domain.exceptions

class InvalidMoneyException(val input: String, reason: String) :
    IllegalArgumentException("Valor monetário inválido \"$input\": $reason")
