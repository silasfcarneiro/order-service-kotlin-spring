package com.silas.order.adapters.`in`.web

import com.silas.order.domain.exception.CatalogUnavailableException
import com.silas.order.domain.exception.OrderNotFoundException
import com.silas.order.domain.exception.ProductUnavailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(ex: OrderNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            "Ordem não encontrado."
        ).apply { title = "Ordem não encontrado" }

    @ExceptionHandler(CatalogUnavailableException::class)
    fun handleCatalogUnavailable(ex: CatalogUnavailableException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Catálogo indisponível no momento. Tente novamente em instantes."
        ).apply { title = "Serviço indisponível" }

    @ExceptionHandler(ProductUnavailableException::class)
    fun handleUnavailable(ex: ProductUnavailableException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.message ?: "Produto indisponível.")
            .apply { title = "Produto indisponível" }

    // regras do domínio (require no init): preço zero, nome vazio, Money inválido...
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleDomainValidation(ex: IllegalArgumentException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Dados inválidos.")
            .apply { title = "Dados inválidos" }

    // Bean Validation dos DTOs (@NotBlank etc.): devolve todos os campos com erro
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(ex: MethodArgumentNotValidException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos.")
            .apply {
                title = "Dados inválidos"
                setProperty("errors", ex.bindingResult.fieldErrors.map {
                    mapOf("field" to it.field, "message" to it.defaultMessage)
                })
            }
}
