package com.silas.order.adapters.out.http.payment

import com.silas.common.domain.Money
import com.silas.order.adapters.out.http.payment.dto.PaymentRequest
import com.silas.order.adapters.out.http.payment.dto.PaymentResponse
import com.silas.order.adapters.out.http.payment.dto.PaymentResult
import com.silas.order.adapters.out.http.payment.dto.toPaymentResult
import com.silas.order.application.port.PaymentClient
import com.silas.order.domain.exception.PaymentUnavailableException
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class HttpPaymentClient(
    @Qualifier("paymentRestClient") private val restClient: RestClient
) : PaymentClient {

    @CircuitBreaker(name = "payment", fallbackMethod = "paymentUnavailable")
    override fun charge(orderId: String, amount: Money): PaymentResult {
        val response = try {
            restClient.post()
                .uri("/v1/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(PaymentRequest(orderId, amount.toString()))
                .retrieve()
                .body<PaymentResponse>()
        } catch (ex: ResourceAccessException) {
            throw PaymentUnavailableException(ex)
        }

        return response?.toPaymentResult() ?: throw PaymentUnavailableException(null)
    }

    // Chamado quando a chamada falha OU quando o circuito está aberto (nem tenta chamar).
    // Tem que ter os mesmos parâmetros do charge + a exceção no final.
    private fun paymentUnavailable(orderId: String, amount: Money, ex: Throwable): PaymentResult =
        throw PaymentUnavailableException(ex)
}
