// UserRegistrationRequest.kt
package com.example.ecommerce.data.model

data class UserRegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String = "Customer"  // Default role set to "Customer"
)
