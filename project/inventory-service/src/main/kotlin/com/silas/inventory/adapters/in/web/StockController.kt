package com.silas.inventory.adapters.`in`.web

import com.silas.inventory.adapters.`in`.web.dto.stock.StockReplenishRequest
import com.silas.inventory.adapters.`in`.web.dto.stock.StockResponse
import com.silas.inventory.adapters.`in`.web.dto.stock.toResponse
import com.silas.inventory.application.StockService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/api/stocks")
class StockController(private val service: StockService) {

    @GetMapping("{sku}")
    fun getStock(@PathVariable("sku") sku: String): ResponseEntity<StockResponse> {
        val stock = service.getStock(sku)
        return ResponseEntity.ok(stock.toResponse())
    }

    @PostMapping("{sku}/replenish")
    fun replenishStock(
        @PathVariable("sku") sku: String,
        @Valid @RequestBody body: StockReplenishRequest
    )
        : ResponseEntity<StockResponse> {
        val result = service.replenish(sku, body.quantity)
        return ResponseEntity.ok(result.toResponse())
    }
}
