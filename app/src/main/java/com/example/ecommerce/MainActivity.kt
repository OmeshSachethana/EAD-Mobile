package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecommerce.ui.login.LoginActivity
import com.example.ecommerce.ui.theme.EcommerceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "User"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"
        val role = intent.getStringExtra(EXTRA_ROLE) ?: "No role"

        setContent {
            EcommerceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar()
                    }
                ) { innerPadding ->
                    MainContent(
                        username = username,
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
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_ROLE = "EXTRA_ROLE"
    }
}

@Composable
fun MainContent(username: String, email: String, role: String, modifier: Modifier = Modifier, onLogoutClick: () -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Hello $username!\nEmail: $email\nRole: $role",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onLogoutClick) {
            Text("Logout")
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    EcommerceTheme {
        MainContent("Android", "android@example.com", "User", onLogoutClick = {})
    }
}
