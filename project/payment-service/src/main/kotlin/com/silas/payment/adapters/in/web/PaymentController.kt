package com.silas.payment.adapters.`in`.web

import com.silas.payment.adapters.`in`.web.dto.*
import com.silas.payment.application.PaymentService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/v1/api/payments")
class PaymentController(private val service: PaymentService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreatePaymentRequest):
        ResponseEntity<PaymentResponse> {
        val result = service.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/v1/api/payments/${result.orderId}"))
            .body(result.toResponse())
    }

    @GetMapping("{orderId}")
    fun search(@PathVariable orderId: String): ResponseEntity<PaymentResponse> {
        val payment = service.getByOrderId(orderId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(payment.toResponse())
    }
}
