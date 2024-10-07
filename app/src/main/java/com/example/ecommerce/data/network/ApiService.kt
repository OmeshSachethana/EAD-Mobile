package com.example.ecommerce.data.network

import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.model.Order
import com.example.ecommerce.data.model.OrderRequest
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.ecommerce.data.model.UserRegistrationRequest
import com.example.ecommerce.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: UserLoginRequest): Call<LoginResponse>

    // User registration request
    @POST("api/users")
    fun register(@Body request: UserRegistrationRequest): Call<User>

    @GET("api/products")
    fun getProducts(): Call<List<Product>>

    @POST("api/orders")
    fun createOrder(@Body order: OrderRequest): Call<Order>

    @PUT("api/orders/{orderId}/payment")
    fun completePayment(@Path("orderId") orderId: String): Call<Void>

}