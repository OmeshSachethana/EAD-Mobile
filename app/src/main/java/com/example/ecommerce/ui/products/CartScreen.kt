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

@Composable
fun CartScreen(
    email: String,
    context: Context,
    cartItems: List<Product>,
    onCartItemsChanged: (List<Product>) -> Unit,
    modifier: Modifier = Modifier
) {
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

            // Align the button at the bottom, outside the scrollable area
            Button(
                onClick = {
                    // Checkout process
                    performCheckout(context) {
                        // Clear cart items after checkout
                        onCartItemsChanged(emptyList())
                    }
                },
                enabled = cartItems.isNotEmpty(), // Disable button if cart is empty
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp) // Optional: Padding at the bottom for better spacing
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

private fun performCheckout(context: Context, onSuccess: () -> Unit) {
    // Simulate a purchase operation (you can replace this with actual purchase logic)
    Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show()

    // Call onSuccess to clear the cart
    onSuccess()
}
