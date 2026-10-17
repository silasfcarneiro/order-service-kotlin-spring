package com.silas.inventory.application

import com.silas.inventory.application.port.StockRepository
import com.silas.inventory.domain.StockItem
import com.silas.inventory.domain.exception.StockItemNotFoundException
import org.springframework.stereotype.Service

@Service
class StockService(private val repository: StockRepository) {

    fun getStock(sku: String): StockItem =
        repository.findBySku(sku) ?: throw StockItemNotFoundException(sku)

    fun replenish(sku: String, quantity: Int): StockItem =
        repository.save(getStock(sku).addStock(quantity))
}
