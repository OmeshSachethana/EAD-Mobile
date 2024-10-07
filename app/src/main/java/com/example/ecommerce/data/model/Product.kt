package com.example.ecommerce.data.model

data class Product(
    val id: String,
    val name: String,
    val vendorId: String,
    val category: String?,
    val description: String?,
    val quantity: Int,
    val price: Double,
    val imageUrl: String?,
    val isActive: Boolean
)