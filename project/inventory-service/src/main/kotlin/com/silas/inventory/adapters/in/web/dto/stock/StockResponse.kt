package com.silas.inventory.adapters.`in`.web.dto.stock

import com.silas.inventory.domain.StockItem

class StockResponse(
    val sku: String,
    val available: Int,
    val reserved: Int,
)

fun StockItem.toResponse(): StockResponse {
    return StockResponse(
        sku = this.sku,
        available = this.available,
        reserved = this.reserved,
    )
}
