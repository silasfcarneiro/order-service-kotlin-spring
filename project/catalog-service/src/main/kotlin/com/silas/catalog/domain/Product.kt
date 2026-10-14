package com.silas.catalog.domain

import com.silas.common.domain.Money

data class Product(
    val sku: String,
    val name: String,
    val price: Money,
    val active: Boolean = true
) {

    init {
        require(name.isNotBlank()) { "O nome não pode ficar em branco." }
        require(sku.isNotBlank()) { "O SKU não pode ficar em branco." }
        require(price > Money.ZERO) { "O preço tem que ser maior que zero." }
    }

    fun changePrice(newPrice: Money): Product {
        if (price == newPrice) return this
        return copy(price = newPrice)
    }

    fun rename(newName: String): Product {
        if (name == newName) return this
        return copy(name = newName)
    }

    fun deactivate(): Product {
        if (!active) return this
        return copy(active = false)
    }
}
