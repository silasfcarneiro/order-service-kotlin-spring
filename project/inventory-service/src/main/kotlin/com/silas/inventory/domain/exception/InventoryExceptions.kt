package com.silas.inventory.domain.exception

class StockItemNotFoundException(sku: String) :
    NotFoundException("Item de estoque não encontrado: $sku.")

class ReservationNotFoundException(orderId: String) :
    NotFoundException("Nenhuma reserva encontrada para o pedido $orderId.")

class ReservationConflictException(orderId: String) :
    ConflictException("O pedido $orderId já possui reserva com itens diferentes.")

class StockItemUnavailableException(skus: List<String>) :
    BusinessRuleException("Itens indisponíveis ou sem estoque suficiente: ${skus.joinToString()}.")

class InsufficientStockException(sku: String, requested: Int, available: Int) :
    BusinessRuleException("Estoque insuficiente para $sku: pedido $requested, disponível $available.")
