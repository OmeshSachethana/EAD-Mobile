package com.example.ecommerce.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.ecommerce.R
import com.example.ecommerce.data.model.LoginResponse
import com.example.ecommerce.data.model.UserLoginRequest
import com.example.ecommerce.data.network.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.auth0.android.jwt.JWT  // Import the JWT library
import com.example.ecommerce.MainActivity
import com.example.ecommerce.data.model.User
import com.example.ecommerce.utils.JwtUtils

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = UserLoginRequest(email, password)

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.token
                        Toast.makeText(this@LoginActivity, "Login Successful. Token: $token", Toast.LENGTH_LONG).show()

                        // Decode the token to extract user information
                        if (token != null) {
                            val user = JwtUtils.decodeToken(token)
                            if (user != null) {
                                navigateToMainActivity(user)
                            } else {
                                Toast.makeText(this@LoginActivity, "Failed to decode token", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed: Unauthorized", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun navigateToMainActivity(user: User) {
        // Create an intent to navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USERNAME, user.name)  // Pass the username to MainActivity
            putExtra(MainActivity.EXTRA_EMAIL, user.email)  // Pass the email to MainActivity
            putExtra(MainActivity.EXTRA_ROLE, user.role)  // Pass the role to MainActivity
        }

        startActivity(intent)
        finish()  // Optional: call finish() if you want to close LoginActivity
    }
}
