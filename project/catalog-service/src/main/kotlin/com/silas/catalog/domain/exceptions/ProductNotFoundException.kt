package com.silas.catalog.domain.exceptions

class ProductNotFoundException(sku: String): IllegalArgumentException("Product not found: $sku")
