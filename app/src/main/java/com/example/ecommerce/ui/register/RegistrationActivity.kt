package com.example.ecommerce.ui.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.data.model.User
import com.example.ecommerce.data.model.UserRegistrationRequest
import com.example.ecommerce.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    private val TAG = "RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        Log.d(TAG, "onCreate called")

        val usernameInput = findViewById<EditText>(R.id.username)
        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.register)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registrationRequest = UserRegistrationRequest(username, email, password)

            RetrofitClient.instance.register(registrationRequest).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@RegistrationActivity, "Registration Successful", Toast.LENGTH_LONG).show()
                        navigateToMainActivity(response.body()!!)
                    } else {
                        Toast.makeText(this@RegistrationActivity, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "Error: ${t.message}")
                    Toast.makeText(this@RegistrationActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun navigateToMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USERNAME, user.name)
            putExtra(MainActivity.EXTRA_EMAIL, user.email)
            putExtra(MainActivity.EXTRA_ROLE, user.role)
        }
        startActivity(intent)
        finish()
    }
}
