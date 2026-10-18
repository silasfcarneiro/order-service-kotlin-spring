package com.silas.order.adapters.out.http.catalog.dto

class CatalogProductResponse(
    val sku: String,
    val name: String,
    val price: String,
    val active: Boolean,
)

class CatalogProductResult(
    val sku: String,
    val name: String,
    val price: String,
    val active: Boolean,
)

fun CatalogProductResponse.toCatalogProduct(): CatalogProductResult {
    return CatalogProductResult(
        sku = sku,
        name = name,
        price = price,
        active = active
    )
}
