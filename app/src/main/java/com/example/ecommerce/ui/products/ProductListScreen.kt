package com.example.ecommerce.ui.products

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import com.example.ecommerce.utils.decodeBase64ToBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product


@Composable
fun ProductListScreen(products: List<Product>, modifier: Modifier = Modifier, onAddToCart: (Product) -> Unit) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(products) { product ->
            ProductItem(product = product, onAddToCart = onAddToCart)
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: (Product) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Decode base64 image and display it if available
        val bitmap = decodeBase64ToBitmap(product.imageUrl)

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp) // Set image size
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop // Crop the image to fill the box
            )
        } else {
            // Fallback image or placeholder if imageUrl is null or decoding fails
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground), // Placeholder image
                contentDescription = "Placeholder",
                modifier = Modifier.size(100.dp)
            )
        }

        // Display product details
        Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = product.description ?: "No description available")
        Text(text = "$${product.price}")

        // Add "Add to Cart" button
        Button(
            onClick = { onAddToCart(product) }, // Handle add to cart action
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Add to Cart")
        }
    }
}


