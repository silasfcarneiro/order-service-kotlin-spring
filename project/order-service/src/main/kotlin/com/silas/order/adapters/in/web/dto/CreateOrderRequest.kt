package com.silas.order.adapters.`in`.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

class CreateOrderRequest(
    @field:NotEmpty val items: List<CreateOrderItemRequest>
) {
    class CreateOrderItemRequest(
        @field:NotBlank val sku: String,
        @field:NotEmpty val quantity: Int
    )
}
