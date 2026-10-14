package com.silas.catalog.adapters.`in`.web.dto

import jakarta.validation.constraints.NotBlank

class UpdateProductRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val price: String
)
