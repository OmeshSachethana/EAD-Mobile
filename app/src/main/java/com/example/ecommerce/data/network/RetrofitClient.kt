package com.example.ecommerce.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.Request

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5207/"  // Replace with your local API URL

    // Method to create an instance of ApiService with an optional token
    fun getInstance(token: String? = null): ApiService {
        val clientBuilder = OkHttpClient.Builder()

        // Add token interceptor if available
        token?.let {
            clientBuilder.addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
        }

        // Create custom Gson to handle BigDecimal properly
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson)) // Use the custom Gson instance
            .build()

        return retrofit.create(ApiService::class.java)
    }
}


