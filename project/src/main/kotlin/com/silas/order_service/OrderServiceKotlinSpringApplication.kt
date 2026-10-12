package com.silas.order_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderServiceKotlinSpringApplication

fun main(args: Array<String>) {
	runApplication<OrderServiceKotlinSpringApplication>(*args)
}
