package com.silas.order.domain.exception

import com.silas.common.domain.OrderId

class OrderNotFoundException(orderId: OrderId) :
    RuntimeException("Pedido não encontrado: $orderId")
