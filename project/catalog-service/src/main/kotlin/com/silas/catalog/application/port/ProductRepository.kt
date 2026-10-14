package com.silas.catalog.application.port

import com.silas.catalog.domain.Product

interface ProductRepository {
    fun findBySku(sku: String): Product?
    fun findBySkus(skus: Collection<String>): List<Product>
    fun findAll(): List<Product>
    fun save(product: Product): Product
}
