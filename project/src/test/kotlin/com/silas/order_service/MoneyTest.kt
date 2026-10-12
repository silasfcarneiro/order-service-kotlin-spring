package com.silas.order_service

import com.silas.order_service.shared.domain.exceptions.InvalidMoneyException
import  com.silas.order_service.shared.domain.Money
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest
class MoneyTest {

    @Test
    fun `testa valores negativos`() {
        val money = Money.of("-100")
        assertEquals("-100.00", money.toString())
    }

    @Test
    fun `testa valores positivos`() {
        val money = Money.of("100")
        assertEquals("100.00", money.toString())
    }

    @Test
    fun `aceita valor com duas casas decimais`() {
        val money = Money.of("19.90")

        assertEquals(BigDecimal("19.90"), money.amount)
    }

    @Test
    fun `completa com zero valor com uma casa decimal`() {
        val money = Money.of("19.9")

        assertEquals(BigDecimal("19.90"), money.amount)
    }

    @Test
    fun `aceita zero`() {
        val money = Money.of("0")

        assertEquals(Money.ZERO, money)
    }

    @Test
    fun `recusa valor com mais de duas casas decimais em vez de arredondar`() {
        val erro = assertFailsWith<InvalidMoneyException> { Money.of("19.999") }

        assertEquals("19.999", erro.input)
    }

    @Test
    fun `recusa virgula como separador decimal`() {
        assertFailsWith<InvalidMoneyException> { Money.of("19,90") }
    }

    @Test
    fun `recusa texto que nao e numero`() {
        assertFailsWith<InvalidMoneyException> { Money.of("abc") }
    }

    @Test
    fun `recusa notacao cientifica`() {
        assertFailsWith<InvalidMoneyException> { Money.of("1E+3") }
    }

    @Test
    fun `recusa texto vazio`() {
        assertFailsWith<InvalidMoneyException> { Money.of("") }
    }

    @Test
    fun `recusa espacos em volta do valor`() {
        assertFailsWith<InvalidMoneyException> { Money.of(" 19.90 ") }
    }
}
