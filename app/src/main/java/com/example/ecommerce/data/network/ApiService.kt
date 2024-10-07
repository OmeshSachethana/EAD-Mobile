package com.example.ecommerce.data.network

import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.ecommerce.data.model.UserRegistrationRequest

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: UserLoginRequest): Call<LoginResponse>

    // User registration request
    @POST("api/users")
    fun register(@Body request: UserRegistrationRequest): Call<User>
}