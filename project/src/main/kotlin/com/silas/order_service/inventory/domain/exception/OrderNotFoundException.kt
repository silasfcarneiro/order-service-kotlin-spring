package com.silas.order_service.inventory.domain.exception

import com.silas.order_service.orders.domain.OrderId

class OrderNotFoundException(
    orderId: OrderId
) : IllegalStateException(
    "Pedido não encontrado $orderId"
)
