package com.silas.order.application.port

import com.silas.order.adapters.out.catalog.CatalogProductResponse

interface PriceCatalog {
    fun findProducts(skus: List<String>): List<CatalogProductResponse>
}
