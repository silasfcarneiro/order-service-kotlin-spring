package com.silas.catalog.domain

import com.silas.catalog.domain.Product
import com.silas.common.domain.Money
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertFailsWith


class ProductTest {

    @Test
    fun `valida criacao do produto com o nome em branco`() {
        assertFailsWith<IllegalArgumentException> {
            Product(
                "teste",
                name = "",
                price = Money.of("10"),
                active = true
            )
        }
    }

    @Test
    fun `valida criacao do produto com o sku em branco`() {
        assertFailsWith<IllegalArgumentException> {
            Product(
                "",
                name = "TV-XYZ",
                price = Money.of("10"),
                active = true
            )
        }
    }

    @Test
    fun `valida criacao do produto com o preco igual a zero`() {
        assertFailsWith<IllegalArgumentException> {
            Product(
                "123456",
                name = "TV-XYZ",
                price = Money.of("0"),
                active = true
            )
        }
    }

    @Test
    fun `valida criacao do produto com o preco menor que zero`() {
        assertFailsWith<IllegalArgumentException> {
            Product(
                "123456",
                name = "TV-XYZ",
                price = Money.of("-1"),
                active = true
            )
        }
    }

    @Test
    fun `valida alterar um preco informando o mesmo preco`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )

        assertEquals(product, product.changePrice(Money.of("10")))
    }

    @Test
    fun `valida alterar um preco informando o preco diferente`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )
        val changed = product.changePrice(Money.of("11"))

        assertEquals(product.copy(price = Money.of("11")), changed)
    }

    @Test
    fun `valida alterar um preco informando um um preco zerado`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )

        assertFailsWith<IllegalArgumentException> {
            product.changePrice(Money.ZERO)
        }
    }

    @Test
    fun `valida alterar um preco informando um um preco menor que zero`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )

        assertFailsWith<IllegalArgumentException> {
            product.changePrice(Money.of("-11"))
        }
    }

    @Test
    fun `valida alterar o nome informando o mesmo nome`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )
        val changed = product.rename("TV-XYZ")
        assertEquals(product.copy(name = "TV-XYZ"), changed)
    }

    @Test
    fun `valida alterar o nome informando um nome diferente`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )
        val changed = product.rename("TV-XZ")

        assertNotEquals(product.name, changed.name)
    }

    @Test
    fun `valida alterar o nome informando um nome em branco`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )

        assertFailsWith<IllegalArgumentException> {
            product.rename("")
        }
    }

    @Test
    fun `tenta desativar um produto já desativado`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = false
        )

        assertEquals(product, product.deactivate())
    }

    @Test
    fun `tenta desativar um produto ativo`() {
        val product = Product(
            "123456",
            name = "TV-XYZ",
            price = Money.of("10"),
            active = true
        )
        val changed = product.deactivate()

        assertNotEquals(product.active, changed.active)
    }
}
