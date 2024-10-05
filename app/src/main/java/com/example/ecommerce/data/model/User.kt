package com.example.ecommerce.data.model

data class User(
    val id: String? = null,
    val username: String,
    val email: String,
    val role: String,
    val password: String,
    val isActive: Boolean = true
)
