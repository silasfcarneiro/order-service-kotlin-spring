package com.silas.catalog.domain.exceptions

class ProductAlreadyExistsException(sku: String) :
    IllegalArgumentException("Product already exists: $sku")
