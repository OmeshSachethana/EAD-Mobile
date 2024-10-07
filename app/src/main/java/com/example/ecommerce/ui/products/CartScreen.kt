package com.example.ecommerce.ui.products

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.data.model.Product

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Order
import com.example.ecommerce.data.model.OrderProductRequest
import com.example.ecommerce.data.model.OrderRequest
import com.example.ecommerce.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CartScreen(
    email: String,
    context: Context,
    cartItems: List<Product>,
    onCartItemsChanged: (List<Product>) -> Unit,
    modifier: Modifier = Modifier
) {
    // State to hold the note entered by the user
    var userNote by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize() // Ensure Column takes up all available space
    ) {
        Text("Your Shopping Cart", style = MaterialTheme.typography.titleLarge)

        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.noun_empty_cart_3592882),
                    contentDescription = "Empty Cart",
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )
                Text(
                    text = "Your cart is empty",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // LazyColumn takes up the available space but not more
                    .fillMaxWidth()
            ) {
                items(cartItems) { product ->
                    CartItem(
                        product = product,
                        onRemoveFromCart = {
                            onCartItemsChanged(cartItems.filter { it.id != product.id })
                        },
                        onIncreaseQuantity = {
                            onCartItemsChanged(cartItems.map {
                                if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                            })
                        },
                        onDecreaseQuantity = {
                            onCartItemsChanged(cartItems.map {
                                if (it.id == product.id && it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
                            }.filter { it.quantity > 0 })
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TextField for user to enter a note
            OutlinedTextField(
                value = userNote,
                onValueChange = { userNote = it },
                label = { Text("Add a note to your order") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Align the button at the bottom, outside the scrollable area
            Button(
                onClick = {
                    // Trigger checkout with the note
                    performCheckout(context, email, cartItems, userNote) {
                        // Clear cart items after checkout
                        onCartItemsChanged(emptyList())
                    }
                },
                enabled = cartItems.isNotEmpty(), // Disable button if cart is empty
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp)
            ) {
                Text("Proceed to Checkout")
            }
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    onRemoveFromCart: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Use AndroidView with Glide to load images
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(200, 200)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                update = { imageView ->
                    Glide.with(imageView.context)
                        .load(product.imageUrl)
                        .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground))
                        .into(imageView)
                },
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Price: $${product.price}", fontWeight = FontWeight.Normal)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecreaseQuantity) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease quantity")
                    }
                    Text("${product.quantity}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onIncreaseQuantity) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Increase quantity")
                    }
                }
            }
            IconButton(onClick = onRemoveFromCart, modifier = Modifier.align(Alignment.CenterVertically)) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove from cart", tint = Color.Red)
            }
        }
    }
}

private fun performCheckout(
    context: Context,
    email: String,
    cartItems: List<Product>,
    userNote: String, // Add user note parameter
    onSuccess: () -> Unit
) {
    // Retrieve the token from SharedPreferences
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("ACCESS_TOKEN", null)

    // Check if token is not null
    if (token.isNullOrEmpty()) {
        Toast.makeText(context, "Error: No token found. Please login again.", Toast.LENGTH_SHORT).show()
        return
    }

    // Construct the OrderRequest object
    val products = cartItems.map { product ->
        OrderProductRequest(
            productId = product.id,
            vendorId = product.vendorId,
            quantity = product.quantity
        )
    }

    val orderRequest = OrderRequest(
        customerId = email,
        products = products,
        notes = userNote  // Add the user-provided note
    )

    // Get the ApiService instance using the retrieved token
    val apiService = RetrofitClient.getInstance(token)

    // Call the ApiService to create the order
    apiService.createOrder(orderRequest).enqueue(object : Callback<Order> {
        override fun onResponse(call: Call<Order>, response: Response<Order>) {
            if (response.isSuccessful) {
                // Order placed successfully, show a success message
                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                onSuccess()  // Clear the cart
            } else {
                // Handle error response from the server
                Toast.makeText(context, "Failed to place order: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Order>, t: Throwable) {
            // Handle network error or failure
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
