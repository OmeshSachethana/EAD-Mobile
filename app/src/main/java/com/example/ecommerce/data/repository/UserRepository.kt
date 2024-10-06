package com.example.ecommerce.data.repository

import android.util.Log
import com.example.ecommerce.data.model.User
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.network.RetrofitInstance
import retrofit2.Response

class UserRepository {

    suspend fun loginUser(request: UserLoginRequest): Response<LoginResponse> {
        return RetrofitInstance.api.loginUser(request)
    }

    suspend fun createUser(user: User): Response<User> {
        return RetrofitInstance.api.createUser(user)
    }

    suspend fun getAllUsers(): Response<List<User>> {
        return RetrofitInstance.api.getAllUsers()
    }

    suspend fun updateUser(id: String, user: User): Response<User> {
        return RetrofitInstance.api.updateUser(id, user)
    }

    suspend fun deleteUser(id: String): Response<Void> {
        return RetrofitInstance.api.deleteUser(id)
    }
}
