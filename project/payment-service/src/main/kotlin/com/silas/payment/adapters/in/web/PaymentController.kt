package com.silas.payment.adapters.`in`.web

import com.silas.payment.adapters.`in`.web.dto.CreatePaymentRequest
import com.silas.payment.adapters.`in`.web.dto.CreatePaymentResponse
import com.silas.payment.adapters.`in`.web.dto.PaymentResponse
import com.silas.payment.adapters.`in`.web.dto.toCommand
import com.silas.payment.adapters.`in`.web.dto.toCreateResponse
import com.silas.payment.adapters.`in`.web.dto.toResponse
import com.silas.payment.application.PaymentService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/v1/api/payments")
class PaymentController(private val service: PaymentService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreatePaymentRequest):
        ResponseEntity<CreatePaymentResponse> {
        val result = service.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/payments/${result.paymentId}"))
            .body(result.toCreateResponse())
    }

    @GetMapping("{orderId}")
    fun search(@PathVariable orderId: String): ResponseEntity<PaymentResponse> {
        val payment = service.getByOrderId(orderId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(payment.toResponse())
    }
}
