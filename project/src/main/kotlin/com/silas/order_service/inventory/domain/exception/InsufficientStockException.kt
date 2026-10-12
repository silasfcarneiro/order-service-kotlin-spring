package com.silas.order_service.inventory.domain.exception

class InsufficientStockException(
    val sku: String,
    val requested: Int,
    val available: Int,
) : IllegalStateException(
    "Estoque insuficiente para $sku: pedido $requested, disponível $available."
)
