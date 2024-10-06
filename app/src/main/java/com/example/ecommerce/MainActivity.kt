package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecommerce.ui.login.LoginActivity
import com.example.ecommerce.ui.theme.EcommerceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the user information passed from the LoginActivity
        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "User"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"
        val role = intent.getStringExtra(EXTRA_ROLE) ?: "No role"

        setContent {
            EcommerceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = username,
                        email = email,
                        role = role,
                        modifier = Modifier.padding(innerPadding),
                        onLogoutClick = { logout() }
                    )
                }
            }
        }
    }

    private fun logout() {
        // Clear user session and navigate back to the LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close the current activity
    }

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_ROLE = "EXTRA_ROLE"
    }
}

@Composable
fun Greeting(name: String, email: String, role: String, modifier: Modifier = Modifier, onLogoutClick: () -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Hello $name!\nEmail: $email\nRole: $role",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onLogoutClick) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceTheme {
        Greeting("Android", "android@example.com", "User", onLogoutClick = {})
    }
}
