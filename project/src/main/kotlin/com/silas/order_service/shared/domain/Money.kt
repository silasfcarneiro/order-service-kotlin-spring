package com.silas.order_service.shared.domain

import com.silas.order_service.shared.domain.exceptions.InvalidMoneyException
import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money private constructor(val amount: BigDecimal) : Comparable<Money> {
//  ↑ value class: em runtime vira só o BigDecimal, sem objeto extra
//                  ↑ construtor privado: ninguém cria Money "cru",
//                    só pelo of(), que garante as 2 casas
//                                                          ↑ permite usar >, <, >=

    operator fun plus(other: Money) = of(amount + other.amount)   // a + b
    operator fun minus(other: Money) = of(amount - other.amount)  // a - b
    operator fun times(quantity: Int) = of(amount * BigDecimal(quantity))  // preço * qtd

    override fun compareTo(other: Money) = amount.compareTo(other.amount)
    override fun toString() = "$amount"

    companion object {
        // Só dígitos, sinal opcional e ponto decimal: recusa "19,90", "1E+3", " 10" e "+10"
        private val FORMATO = Regex("-?\\d+(\\.\\d+)?")

        val ZERO = of(BigDecimal.ZERO)

        // A única porta de entrada: todo Money nasce com 2 casas e HALF_EVEN
        private fun of(value: BigDecimal) = Money(value.setScale(2, RoundingMode.HALF_EVEN))

        // Entrada externa não é arredondada: mais de 2 casas é erro de quem enviou (ADR-0001)
        fun of(value: String): Money {
            if (!FORMATO.matches(value)) {
                throw InvalidMoneyException(value, "formato inválido, use ponto como separador decimal (ex.: 19.90)")
            }
            if (value.substringAfter('.', "").length > 2) {
                throw InvalidMoneyException(value, "mais de 2 casas decimais")
            }
            return of(BigDecimal(value))
        }
    }
}
