package com.example.ecommerce.data.model

data class OrdersResponse(
    val message: String,
    val orders: List<Order>
)