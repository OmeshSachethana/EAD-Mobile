package com.example.ecommerce.data.model

data class OrderRequest(
    val customerId: String,           // User's email (assuming email is used as the customerId)
    val products: List<OrderProductRequest>,
    val notes: String? = null
)