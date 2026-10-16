package com.silas.order.adapters.out.catalog

class CatalogProductResponse(
    val sku: String,
    val name: String,
    val price: String,
    val active: Boolean,
)

fun CatalogProductResponse.toCatalogProduct(): CatalogProductResponse {
    return CatalogProductResponse(
        sku = sku,
        name = name,
        price = price,
        active = active
    )
}
