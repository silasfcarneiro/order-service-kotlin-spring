package com.silas.inventory.adapters.`in`.web

import com.silas.inventory.domain.exception.BusinessRuleException
import com.silas.inventory.domain.exception.ConflictException
import com.silas.inventory.domain.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    // 404 - recurso não existe
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ProblemDetail =
        problem(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex)

    // 409 - conflito de estado: repetição com dados diferentes ou transição inválida
    // (o check() do domínio lança IllegalStateException, ex.: confirmar reserva devolvida)
    @ExceptionHandler(ConflictException::class, IllegalStateException::class)
    fun handleConflict(ex: RuntimeException): ProblemDetail =
        problem(HttpStatus.CONFLICT, "Conflito de estado", ex)

    // 422 - requisição válida, mas a regra de negócio impede (ex.: sem estoque)
    @ExceptionHandler(BusinessRuleException::class)
    fun handleBusinessRule(ex: BusinessRuleException): ProblemDetail =
        problem(HttpStatus.UNPROCESSABLE_CONTENT, "Regra de negócio violada", ex)

    // 400 - require() do domínio: quantidade zero, sku vazio...
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidArgument(ex: IllegalArgumentException): ProblemDetail =
        problem(HttpStatus.BAD_REQUEST, "Dados inválidos", ex)

    // 400 - Bean Validation dos DTOs: devolve todos os campos com erro
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(ex: MethodArgumentNotValidException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos.")
            .apply {
                title = "Dados inválidos"
                setProperty("errors", ex.bindingResult.fieldErrors.map {
                    mapOf("field" to it.field, "message" to it.defaultMessage)
                })
            }

    private fun problem(status: HttpStatus, title: String, ex: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(status, ex.message ?: title)
            .apply { this.title = title }
}
