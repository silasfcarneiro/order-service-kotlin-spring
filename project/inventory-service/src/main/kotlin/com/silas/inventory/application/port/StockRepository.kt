package com.silas.inventory.application.port

import com.silas.inventory.domain.StockItem

interface StockRepository {
    fun findAllBySkus(skus: List<String>): List<StockItem>
    fun saveAll(reservedStock: List<StockItem>): List<StockItem>
    fun findBySku(sku: String): StockItem?
    fun save(stock: StockItem): StockItem
}
