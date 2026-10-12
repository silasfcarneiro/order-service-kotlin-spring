package com.silas.order_service.inventory.domain

import com.silas.order_service.inventory.domain.exception.InsufficientStockException
import com.silas.order_service.shared.domain.ensure

data class StockItem (
    val sku: String,
    val available: Int,
    val reserved: Int,
) {

    init {
        require(available >= 0) { "A quantidade disponivel tem que ser maior ou igual que zero." }
        require(reserved >= 0) { "A quantidade reservada tem que ser maior ou igual que zero." }
    }

    fun reserve(quantity: Int): StockItem {
        require(quantity > 0) { "A quantidade tem que ser maior que zero." }
        ensure(available >= quantity) { InsufficientStockException(sku, quantity, available) }
        return copy(available = available - quantity, reserved = reserved + quantity)
    }

    fun release(quantity: Int): StockItem {
        require(quantity > 0) { "A quantidade tem que ser maior que zero." }
        check(reserved >= quantity) { "Não é possível devolver $quantity de $sku: só há $reserved reservados." }
        return copy(available = available + quantity, reserved = reserved - quantity)
    }

    fun confirm(quantity: Int): StockItem {
        require(quantity > 0) { "A quantidade tem que ser maior que zero." }
        check(reserved >= quantity) { "Não é possível confirmar $quantity de $sku: só há $reserved reservados." }
        return copy(reserved = reserved - quantity)
    }
}
