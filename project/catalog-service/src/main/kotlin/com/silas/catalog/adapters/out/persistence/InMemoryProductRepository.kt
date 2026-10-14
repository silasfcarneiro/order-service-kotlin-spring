package com.silas.catalog.adapters.out.persistence

import com.silas.catalog.application.port.ProductRepository
import com.silas.catalog.domain.Product
import com.silas.common.domain.Money
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryProductRepository : ProductRepository {

    private val products = ConcurrentHashMap<String, Product>()

    init {
        listOf(
            Product(sku = "TV-50", name = "TV 50 polegadas", price = Money.of("2999.90")),
            Product(sku = "CABO-HDMI", name = "Cabo HDMI 2m", price = Money.of("49.90")),
            Product(sku = "SOUNDBAR", name = "Soundbar", price = Money.of("899.00")),
            Product(sku = "TV-32-OLD", name = "TV 32 (descontinuada)", price = Money.of("1299.00"))
                .deactivate(),
        ).forEach { save(it) }
    }

    override fun findBySku(sku: String): Product? = products[sku]

    override fun findBySkus(skus: Collection<String>): List<Product> =
        skus.mapNotNull { products[it] }

    override fun findAll(): List<Product> = products.values.sortedBy { it.sku }

    override fun save(product: Product): Product {
        products[product.sku] = product
        return product
    }
}
