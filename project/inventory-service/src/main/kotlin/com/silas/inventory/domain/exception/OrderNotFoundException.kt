package com.silas.inventory.domain.exception

import com.silas.common.domain.OrderId


class OrderNotFoundException(
    orderId: OrderId
) : IllegalStateException(
    "Pedido não encontrado $orderId"
)
