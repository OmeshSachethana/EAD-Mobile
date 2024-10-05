package com.example.ecommerce.data.network

import com.example.ecommerce.data.model.User
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.network.RetrofitInstance

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("users/login")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<LoginResponse>

    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Void>
}

