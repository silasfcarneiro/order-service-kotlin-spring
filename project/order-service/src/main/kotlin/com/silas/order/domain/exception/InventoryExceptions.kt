package com.silas.order.domain.exception

import org.springframework.web.client.ResourceAccessException

class InventoryUnavailableException(ex: ResourceAccessException) :
    IllegalArgumentException(ex.message ?: "Estoque indisponível.")

class StockUnavailableException(orderId: String):
    IllegalArgumentException("Não possui estoque disponível para o pedido: $orderId")

class InventoryCannotConfirmException(ex: ResourceAccessException?):
    IllegalArgumentException(ex?.message ?: "Não foi possível confirmar o estoque.")

class InventoryCannotReleaseException(ex: ResourceAccessException?):
    IllegalArgumentException(ex?.message ?: "Não foi possível fazer a devolução de estoque.")
