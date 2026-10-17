package com.silas.inventory.adapters.out.persistence

import com.silas.inventory.application.port.StockRepository
import com.silas.inventory.domain.StockItem
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryStockRepository: StockRepository {

    private val stocks = ConcurrentHashMap<String, StockItem>()

    init {
        listOf(
            StockItem(sku = "TV-50", available = 1000, reserved = 0),
            StockItem(sku = "CABO-HDMI", available = 1000, reserved = 0),
            StockItem(sku = "SOUNDBAR", available = 1000, reserved = 0),
            StockItem(sku = "TV-32-OLD", available = 1000, reserved = 0),
        ).forEach { save(it) }
    }

    override fun findAllBySkus(skus: List<String>): List<StockItem> =
        stocks.values.filter { it.sku in skus }

    override fun saveAll(reservedStock: List<StockItem>): List<StockItem> {
        reservedStock.forEach { stocks[it.sku] = it }
        return reservedStock
    }

    override fun findBySku(sku: String): StockItem? = stocks[sku]

    override fun save(stock: StockItem): StockItem {
        stocks[stock.sku] = stock
        return stock
    }
}
