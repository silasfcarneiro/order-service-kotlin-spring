package com.silas.catalog.adapters.`in`.web.dto

import com.silas.catalog.domain.Product
import com.silas.common.domain.Money

fun CreateProductRequest.toDomain(): Product =
    Product(
        sku = sku,
        name = name,
        price = Money.of(price),
    )

fun Product.toResponse(): ProductResponse =
    ProductResponse(
        sku = sku,
        name = name,
        price = price.amount.toPlainString(),
        active = active,
    )
