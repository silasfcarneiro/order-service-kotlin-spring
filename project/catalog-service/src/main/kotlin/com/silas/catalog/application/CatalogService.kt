package com.silas.catalog.application

import com.silas.catalog.application.port.ProductRepository
import com.silas.catalog.domain.Product
import com.silas.catalog.domain.exceptions.ProductAlreadyExistsException
import com.silas.catalog.domain.exceptions.ProductNotFoundException
import com.silas.common.domain.Money
import org.springframework.stereotype.Service

@Service
class CatalogService(
    private val repository: ProductRepository
) {

    fun getProduct(sku: String): Product =
        repository.findBySku(sku) ?: throw ProductNotFoundException(sku)

    fun getProductBySkus(skus: Collection<String>): List<Product> = repository.findBySkus(skus)

    fun getAllProducts(): List<Product> = repository.findAll()

    fun createProduct(product: Product): Product {
        if (repository.findBySku(product.sku) != null)
            throw ProductAlreadyExistsException(product.sku)

        val result = repository.save(product)
        return result
    }

    fun updateProduct(sku: String, name: String, price: Money): Product {
        val existing = repository.findBySku(sku)
            ?: throw ProductNotFoundException(sku)

        val changed = existing
            .rename(name)
            .changePrice(price)

        return repository.save(
            changed
        )
    }

    fun deactivateProduct(sku: String): Product {
        val product = repository.findBySku(sku)
            ?: throw ProductNotFoundException(sku)

        return repository.save(
            product.deactivate()
        )
    }
}
