package com.silas.catalog.adapters.`in`.web.dto

import jakarta.validation.constraints.NotBlank

class CreateProductRequest (
    @field:NotBlank val sku: String,
    @field:NotBlank val name: String,
    @field:NotBlank val price: String,
)
