package com.silas.payment.adapters.`in`.web

import com.silas.payment.domain.exceptions.PaymentConflictException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(PaymentConflictException::class)
    fun handleUnavailable(ex: PaymentConflictException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.message ?: "Conflito de pedido.")
            .apply { title = "Conflito de pedido" }

    // regras do domínio (require no init): Money inválido...
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
