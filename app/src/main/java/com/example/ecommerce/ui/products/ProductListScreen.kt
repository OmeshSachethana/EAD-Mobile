package com.example.ecommerce.ui.products

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.utils.decodeBase64ToBitmap

@Composable
fun ProductListScreen(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onAddToCart: (Product) -> Unit
) {
    // State variables for search and category filter
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // List of categories (including "All" for no filtering)
    val categories = listOf(
        "All", "Biscuits", "Canned Foods", "Snacks", "Dairy Products",
        "Frozen Foods", "Beverages", "Condiments", "Grains", "Fresh Produce",
        "Bakery", "Stationery"
    )

    // Filter products based on search query and selected category
    val filteredProducts = products.filter { product ->
        (selectedCategory == "All" || product.category == selectedCategory) &&
                product.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.padding(8.dp)) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search products...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Category dropdown
        CategoryDropdownMenu(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            categories = categories
        )

        // Product list
        LazyColumn {
            items(filteredProducts) { product ->
                ProductItem(product = product, onAddToCart = onAddToCart)
            }
        }
    }
}

@Composable
fun CategoryDropdownMenu(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Button(onClick = { expanded = true }) {
            Text(text = selectedCategory) // Display the currently selected category
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    text = {
                        Text(text = category) // Correctly passing the Text composable for the 'text' parameter
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: (Product) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        val bitmap = decodeBase64ToBitmap(product.imageUrl)

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Placeholder",
                modifier = Modifier.size(100.dp)
            )
        }

        Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = product.description ?: "No description available")
        Text(text = "$${product.price}")

        Button(
            onClick = { onAddToCart(product) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Add to Cart")
        }
    }
}
