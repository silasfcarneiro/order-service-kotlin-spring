package com.silas.inventory.domain.exception

abstract class NotFoundException(message: String) : RuntimeException(message)

/** A operação conflita com o estado atual (repetição com dados diferentes, transição inválida). */
abstract class ConflictException(message: String) : RuntimeException(message)

/** A requisição está correta, mas uma regra de negócio impede a operação. */
abstract class BusinessRuleException(message: String) : RuntimeException(message)
