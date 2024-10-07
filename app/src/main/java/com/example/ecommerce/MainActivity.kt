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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.ui.theme.EcommerceTheme
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.ui.login.LoginActivity
import com.example.ecommerce.ui.products.ProductListScreen
import com.example.ecommerce.ui.profile.ProfileScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get access token from shared preferences
        val token = getAccessTokenFromSharedPreferences()
        Log.d("MainActivity", "Access Token: $token")

        setContent {
            var selectedItem by remember { mutableStateOf("Home") }
            var products by remember { mutableStateOf<List<Product>>(emptyList()) }
            var cartItems by remember { mutableStateOf<List<Product>>(emptyList()) }

            EcommerceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(selectedItem = selectedItem) { selected ->
                            selectedItem = selected
                        }
                    }
                ) { innerPadding ->
                    when (selectedItem) {
                        "Home" -> {
                            // Ensure fetchProducts is only called once when Home is displayed
                            LaunchedEffect(selectedItem) {
                                ProductRepository(token).fetchProducts(
                                    onSuccess = { productList ->
                                        products = productList
                                    },
                                    onError = { error ->
                                        Log.e("MainActivity", "Error fetching products: ${error.message}")
                                    }
                                )
                            }
                            // Pass the onAddToCart callback
                            ProductListScreen(products, modifier = Modifier.padding(innerPadding)) { product ->
                                cartItems = cartItems + product // Add product to cart
                                Log.d("MainActivity", "Added to cart: ${product.name}")
                            }
                        }
                        "Cart" -> CartScreen(cartItems, modifier = Modifier.padding(innerPadding))
                        "Profile" -> {
                            val username = intent.getStringExtra(EXTRA_USERNAME) ?: "User"
                            val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"
                            val role = intent.getStringExtra(EXTRA_ROLE) ?: "No role"
                            ProfileScreen(username, email, role, modifier = Modifier.padding(innerPadding)) {
                                onLogout() // Handle logout here
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onLogout() {
        // Clear access token from shared preferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("ACCESS_TOKEN")
            apply()
        }

        // Navigate to login screen
        val intent = Intent(this, LoginActivity::class.java) // Replace with your actual login activity
        startActivity(intent)
        finish() // Optional: finish the current activity
    }

    private fun getAccessTokenFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("ACCESS_TOKEN", null) ?: ""
    }

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_ROLE = "EXTRA_ROLE"
    }
}

@Composable
fun CartScreen(cartItems: List<Product>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Cart Screen", style = MaterialTheme.typography.titleLarge)
        if (cartItems.isEmpty()) {
            Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(cartItems) { product ->
                    Text(text = product.name)
                    Text(text = "$${product.price}")
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: String, onItemSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedItem == "Home",
            onClick = {
                onItemSelected("Home")
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedItem == "Cart",
            onClick = {
                onItemSelected("Cart")
            },
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = {
                onItemSelected("Profile")
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
