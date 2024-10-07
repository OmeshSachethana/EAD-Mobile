package com.example.ecommerce.data.repository

import android.util.Log
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val token: String) {

    // RetrofitClient will handle the token automatically via the interceptor
    private val apiService = RetrofitClient.getInstance(token)

    fun fetchProducts(onSuccess: (List<Product>) -> Unit, onError: (Throwable) -> Unit) {
        apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    } ?: onError(Exception("No products found"))
                } else {
                    Log.e("ProductRepository", "Error: ${response.code()} - ${response.message()}")
                    onError(Exception("Failed to fetch products"))
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("ProductRepository", "Network call failed: ${t.message}")
                onError(t)
            }
        })
    }
}


