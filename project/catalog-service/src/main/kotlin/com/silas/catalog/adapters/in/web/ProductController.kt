package com.silas.catalog.adapters.`in`.web

import com.silas.catalog.adapters.`in`.web.dto.CreateProductRequest
import com.silas.catalog.adapters.`in`.web.dto.ProductResponse
import com.silas.catalog.adapters.`in`.web.dto.UpdateProductRequest
import com.silas.catalog.adapters.`in`.web.dto.toDomain
import com.silas.catalog.adapters.`in`.web.dto.toResponse
import com.silas.catalog.application.CatalogService
import com.silas.common.domain.Money
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/v1/api/products")
class ProductController (
    private val catalogService: CatalogService
) {

    @GetMapping("{sku}")
    fun getProduct(@PathVariable sku: String): ResponseEntity<ProductResponse> {
        val product = catalogService.getProduct(sku)
        return ResponseEntity.ok(product.toResponse())
    }

    @GetMapping
    fun getProducts(@RequestParam(required = false) skus: List<String>?): ResponseEntity<List<ProductResponse>> {
        val products = if (skus.isNullOrEmpty()) {
            catalogService.getAllProducts()
        } else {
            catalogService.getProductBySkus(skus)
        }
        return ResponseEntity.ok(products.map { it.toResponse() })
    }

    @PostMapping
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val product = catalogService.createProduct(request.toDomain())
        return ResponseEntity
            .created(URI.create("/v1/api/products/${product.sku}"))
            .body(product.toResponse())
    }

    @PutMapping("{sku}")
    fun updateProduct(
        @PathVariable sku: String,
        @Valid @RequestBody request: UpdateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = catalogService.updateProduct(sku, request.name, Money.of(request.price))
        return ResponseEntity.ok(product.toResponse())
    }

    @PatchMapping("{sku}/deactivate")
    fun patchProduct(@PathVariable sku: String):
        ResponseEntity<ProductResponse> {
        val response = catalogService.deactivateProduct(sku)
        return ResponseEntity.ok(response.toResponse())
    }
}
