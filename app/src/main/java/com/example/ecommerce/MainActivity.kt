package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.ui.theme.EcommerceTheme
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.filled.LineStyle
import androidx.compose.runtime.LaunchedEffect
import com.example.ecommerce.data.model.Order
import com.example.ecommerce.data.model.OrdersResponse
import com.example.ecommerce.data.network.RetrofitClient
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.ui.login.LoginActivity
import com.example.ecommerce.ui.orders.OrderScreen
import com.example.ecommerce.ui.products.CartScreen
import com.example.ecommerce.ui.products.ProductListScreen
import com.example.ecommerce.ui.products.loadCartItems
import com.example.ecommerce.ui.products.saveCartItems
import com.example.ecommerce.ui.profile.ProfileScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get access token from shared preferences
        val token = getAccessTokenFromSharedPreferences()
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"

        setContent {
            var selectedItem by remember { mutableStateOf("Home") }
            var products by remember { mutableStateOf<List<Product>>(emptyList()) }
            var cartItems by remember { mutableStateOf(loadCartItems(this, email)) }
            var orders by remember { mutableStateOf<List<Order>>(emptyList()) }

            // Save cart items whenever they change
            LaunchedEffect(cartItems) {
                saveCartItems(this@MainActivity, email, cartItems) // Save using email
            }

            // Fetch orders when the "Orders" tab is selected or when payment is complete
            fun fetchOrders() {
                val apiService = RetrofitClient.getInstance(token)
                apiService.getOrders().enqueue(object : Callback<OrdersResponse> {
                    override fun onResponse(call: Call<OrdersResponse>, response: Response<OrdersResponse>) {
                        if (response.isSuccessful) {
                            // Extract the orders from the response
                            orders = response.body()?.orders ?: emptyList()
                        } else {
                            Log.e("MainActivity", "Failed to fetch orders: ${response.message()}")
                            Toast.makeText(this@MainActivity, "Failed to load orders.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                        Log.e("MainActivity", "Error fetching orders: ${t.message}")
                        Toast.makeText(this@MainActivity, "Error fetching orders.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            LaunchedEffect(selectedItem) {
                if (selectedItem == "Orders") {
                    fetchOrders() // Fetch orders when "Orders" tab is selected
                }
            }

            EcommerceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(selectedItem = selectedItem) { selected -> selectedItem = selected }
                    }
                ) { innerPadding ->
                    when (selectedItem) {
                        "Home" -> {
                            // Ensure fetchProducts is only called once when Home is displayed
                            LaunchedEffect(selectedItem) {
                                ProductRepository(token).fetchProducts(
                                    onSuccess = { productList -> products = productList },
                                    onError = { error -> Log.e("MainActivity", "Error fetching products: ${error.message}") }
                                )
                            }
                            ProductListScreen(
                                products = products,
                                modifier = Modifier.padding(innerPadding)
                            ) { product ->
                                val existingProduct = cartItems.find { it.id == product.id }

                                if (existingProduct != null) {
                                    // If the product is already in the cart, increase the quantity
                                    cartItems = cartItems.map {
                                        if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                                    }
                                } else {
                                    // If the product is new to the cart, add it with its current quantity from the DB
                                    cartItems = cartItems + product // Keep the product's existing quantity from DB
                                }
                            }
                        }
                        "Cart" -> CartScreen(
                            email = email,
                            context = this, // Pass the context to CartScreen
                            cartItems = cartItems, // Pass cartItems state
                            onCartItemsChanged = { updatedCartItems -> cartItems = updatedCartItems }, // Allow CartScreen to update cartItems
                            modifier = Modifier.padding(innerPadding)
                        )
                        "Profile" -> {
                            val username = intent.getStringExtra(EXTRA_USERNAME) ?: "User"
                            val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"
                            val role = intent.getStringExtra(EXTRA_ROLE) ?: "No role"
                            ProfileScreen(username, email, role, modifier = Modifier.padding(innerPadding)) {
                                onLogout()
                            }
                        }
                        "Orders" -> {
                            OrderScreen(
                                token = token,
                                orders = orders,
                                modifier = Modifier.padding(innerPadding),
                                onPaymentComplete = { fetchOrders() } // Refresh orders after payment
                            )
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
            selected = selectedItem == "Orders",
            onClick = {
                onItemSelected("Orders")
            },
            icon = { Icon(Icons.Filled.LineStyle, contentDescription = "Orders") },
            label = { Text("Orders") }
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
