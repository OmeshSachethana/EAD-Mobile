package com.example.ecommerce.data.network

import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.ecommerce.data.model.UserRegistrationRequest
import com.example.ecommerce.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: UserLoginRequest): Call<LoginResponse>

    // User registration request
    @POST("api/users")
    fun register(@Body request: UserRegistrationRequest): Call<User>

    @GET("api/products")
    fun getProducts(): Call<List<Product>>
}