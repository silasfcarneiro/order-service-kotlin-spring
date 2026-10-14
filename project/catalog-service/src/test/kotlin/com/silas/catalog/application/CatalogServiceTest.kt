package com.silas.catalog.application

import com.silas.catalog.application.port.ProductRepository
import com.silas.catalog.domain.Product
import com.silas.catalog.domain.exceptions.ProductNotFoundException
import com.silas.common.domain.Money
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CatalogServiceTest {

    private class FakeProductRepository(vararg products: Product) : ProductRepository {
        private val products = products.associateBy { it.sku }.toMutableMap()

        override fun findBySku(sku: String): Product? = products[sku]
        override fun findBySkus(skus: Collection<String>): List<Product> = skus.mapNotNull { products[it] }
        override fun findAll(): List<Product> = products.values.toList()
        override fun save(product: Product): Product {
            products[product.sku] = product
            return product
        }
    }

    private val tv = Product(sku = "TV-50", name = "TV 50 polegadas", price = Money.of("2999.90"))

    @Test
    fun `busca produto existente pelo sku`() {
        val service = CatalogService(FakeProductRepository(tv))

        assertEquals(tv, service.getProduct("TV-50"))
    }

    @Test
    fun `busca produto inexistente lanca ProductNotFoundException`() {
        val service = CatalogService(FakeProductRepository(tv))

        assertFailsWith<ProductNotFoundException> { service.getProduct("NAO-EXISTE") }
    }

    @Test
    fun `atualiza nome e preco do produto pelo sku do path`() {
        val repository = FakeProductRepository(tv)
        val service = CatalogService(repository)

        val updated = service.updateProduct("TV-50", "TV 50 4K", Money.of("3199.90"))

        assertEquals(tv.copy(name = "TV 50 4K", price = Money.of("3199.90")), updated)
        assertEquals(updated, repository.findBySku("TV-50"))
    }

    @Test
    fun `atualiza produto inexistente lanca ProductNotFoundException`() {
        val service = CatalogService(FakeProductRepository(tv))

        assertFailsWith<ProductNotFoundException> {
            service.updateProduct("NAO-EXISTE", "Qualquer", Money.of("10.00"))
        }
    }
}
