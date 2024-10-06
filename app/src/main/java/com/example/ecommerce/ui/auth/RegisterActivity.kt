package com.example.ecommerce.ui.auth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.R
import com.example.ecommerce.data.model.User
import com.example.ecommerce.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val userRepository = UserRepository()
    private val TAG = "RegisterActivity" // Tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Find the input fields and register button in the layout
        val usernameField = findViewById<EditText>(R.id.username_input)
        val emailField = findViewById<EditText>(R.id.email_input)
        val passwordField = findViewById<EditText>(R.id.password_input)
        val registerButton = findViewById<Button>(R.id.register_button)

        // Log to ensure onCreate is running
        Log.d(TAG, "onCreate called")

        // Set up registration logic when the button is clicked
        registerButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            Log.d(TAG, "Register button clicked") // Log the button click

            // Validate inputs before making the request
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                createUser(username, email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(username: String, email: String, password: String) {
        val user = User(username = username, email = email, role = "Customer", password = password)
        Log.d(TAG, "Attempting to create user: $user")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userRepository.createUser(user)
                Log.d(TAG, "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    Log.d(TAG, "Registration successful: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        // Navigate to the login screen or home screen here
                    }
                } else {
                    Log.e(TAG, "Registration failed: ${response.code()}, Message: ${response.errorBody()?.string()}")
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception occurred: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
