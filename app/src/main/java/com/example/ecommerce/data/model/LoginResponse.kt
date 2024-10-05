package com.example.ecommerce.data.model

data class LoginResponse(
    val token: String,  // Assuming your API returns a token
    val user: User
)
