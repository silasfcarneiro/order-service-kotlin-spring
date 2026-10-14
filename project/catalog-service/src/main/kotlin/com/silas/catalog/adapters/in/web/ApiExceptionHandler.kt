package com.silas.catalog.adapters.`in`.web

import com.silas.catalog.domain.exceptions.ProductAlreadyExistsException
import com.silas.catalog.domain.exceptions.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleNotFound(ex: ProductNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Produto não encontrado.")
            .apply { title = "Produto não encontrado" }

    @ExceptionHandler(ProductAlreadyExistsException::class)
    fun handleConflict(ex: ProductAlreadyExistsException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Produto já cadastrado.")
            .apply { title = "Produto já cadastrado" }

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
