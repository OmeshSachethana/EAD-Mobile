package com.example.ecommerce.data.model

data class OrderProductRequest(
    val productId: String,
    val vendorId: String,
    val quantity: Int
)