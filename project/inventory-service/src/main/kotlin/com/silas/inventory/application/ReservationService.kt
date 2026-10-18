package com.silas.inventory.application

import com.silas.inventory.application.command.ReservationCreateCommand
import com.silas.inventory.application.port.ReservationRepository
import com.silas.inventory.application.port.StockRepository
import com.silas.inventory.domain.Reservation
import com.silas.inventory.domain.ReservationId
import com.silas.inventory.domain.StockItem
import com.silas.inventory.domain.exception.ReservationConflictException
import com.silas.inventory.domain.exception.ReservationNotFoundException
import com.silas.inventory.domain.exception.StockItemUnavailableException
import org.springframework.stereotype.Service
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class ReservationService(
    private val repository: ReservationRepository,
    private val stockRepository: StockRepository
) {
    // Uma operação de estoque por vez: evita o "lost update" entre ler e salvar.
    // ReentrantLock em vez de synchronized para não prender threads virtuais (pinning no Java 21).
    private val lock = ReentrantLock()

    fun create(command: ReservationCreateCommand): List<Reservation> = lock.withLock {
        val existing = repository.findByOrderId(command.orderId)
        if (existing.isNotEmpty()) return existingOrConflict(existing, command)

        val stockBySku = stockRepository
            .findAllBySkus(command.items.map { it.sku })
            .associateBy { it.sku }

        val unavailable = command.items
            .filter { stockBySku[it.sku]?.canReserve(it.quantity) != true }
            .map { it.sku }
        if (unavailable.isNotEmpty()) throw StockItemUnavailableException(unavailable)

        val reservedStock = command.items.map { stockBySku.getValue(it.sku).reserve(it.quantity) }
        val reservations = command.items.map {
            Reservation(ReservationId.new(), command.orderId, it.sku, it.quantity)
        }

        stockRepository.saveAll(reservedStock)
        return repository.saveAll(reservations)
    }

    fun confirm(orderId: String) =
        transition(orderId, { it.confirm() }, { stock, qty -> stock.confirm(qty) })

    fun release(orderId: String) =
        transition(orderId, { it.release() }, { stock, qty -> stock.release(qty) })

    private fun existingOrConflict(
        existing: List<Reservation>,
        command: ReservationCreateCommand
    ): List<Reservation> {
        val sameItems = existing.size == command.items.size &&
            command.items.all { item -> existing.any { it.matches(item.sku, item.quantity) } }
        if (sameItems) throw ReservationConflictException(command.orderId)
        return existing
    }

    private fun transition(
        orderId: String,
        changeReservation: (Reservation) -> Reservation,
        changeStock: (StockItem, Int) -> StockItem
    ): List<Reservation> = lock.withLock {
        val before = repository.findByOrderId(orderId)
        if (before.isEmpty()) throw ReservationNotFoundException(orderId)

        val after = before.map(changeReservation)
        val changed = before.zip(after)
            .filter { (old, new) -> old != new }
            .map { (_, new) -> new }

        if (changed.isNotEmpty()) {
            val stockBySku = stockRepository
                .findAllBySkus(changed.map { it.sku })
                .associateBy { it.sku }
            stockRepository.saveAll(changed.map { changeStock(stockBySku.getValue(it.sku), it.quantity) })
            repository.saveAll(changed)
        }
        return after
    }
}
