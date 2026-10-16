package com.silas.order.domain.exception

class ProductUnavailableException(val skus: List<String>) :
    IllegalArgumentException("Product unavailable: ${skus.joinToString(", ")}")
