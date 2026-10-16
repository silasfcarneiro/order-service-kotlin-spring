package com.silas.order.domain.exception

class CatalogUnavailableException(cause: Throwable) :
    RuntimeException("Catálogo indisponível", cause)
