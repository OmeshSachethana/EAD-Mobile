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
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce.R

@Composable
fun CartScreen(
    email: String,
    context: Context,
    modifier: Modifier = Modifier
) {
    // Load cart items using the email
    var cartItems by remember { mutableStateOf(loadCartItems(context, email)) }

    // Save cart items whenever they change
    LaunchedEffect(cartItems) {
        saveCartItems(context, email, cartItems)  // Use email for saving
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Your Shopping Cart", style = MaterialTheme.typography.titleLarge)

        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(cartItems) { product ->
                    CartItem(
                        product = product,
                        onRemoveFromCart = {
                            cartItems = cartItems.filter { it.id != product.id }
                        },
                        onIncreaseQuantity = {
                            cartItems = cartItems.map {
                                if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                            }
                        },
                        onDecreaseQuantity = {
                            cartItems = cartItems.map {
                                if (it.id == product.id && it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
                            }.filter { it.quantity > 0 }
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
            Button(onClick = { /* Proceed to checkout */ }, modifier = Modifier.align(Alignment.End)) {
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

