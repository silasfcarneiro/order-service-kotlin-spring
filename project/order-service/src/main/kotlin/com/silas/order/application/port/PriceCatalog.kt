package com.silas.order.application.port

import com.silas.order.adapters.out.http.catalog.dto.CatalogProductResult

interface PriceCatalog {
    fun findProducts(skus: List<String>): List<CatalogProductResult>
}
