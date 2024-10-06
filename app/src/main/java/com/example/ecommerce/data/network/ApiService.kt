package com.example.ecommerce.data.network

import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.model.UserLoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: UserLoginRequest): Call<LoginResponse>
}

