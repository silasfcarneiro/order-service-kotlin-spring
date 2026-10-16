package com.silas.order.adapters.`in`.web.dto

import com.silas.order.application.command.OrderCreateCommand
import com.silas.order.domain.Order

fun CreateOrderRequest.toCommand(): OrderCreateCommand {
    return OrderCreateCommand(
        items = this.items.map {
            OrderCreateCommand.Item(
                sku = it.sku,
                quantity = it.quantity
            )
        }
    )
}

fun Order.toResponse(): CreateOrderResponse {
    return CreateOrderResponse(
        orderId = this.orderId,
    )
}
