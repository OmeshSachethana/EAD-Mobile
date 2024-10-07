package com.example.ecommerce.ui.products

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.utils.decodeBase64ToBitmap

@OptIn(ExperimentalFoundationApi::class)
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

        // Horizontal scrollable category buttons
        CategoryButtons(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            categories = categories
        )

        // Product grid with two columns
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Define 2 columns
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { product ->
                ProductItem(product = product, onAddToCart = onAddToCart)
            }
        }
    }
}

@Composable
fun CategoryButtons(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>
) {
    // Horizontal scrolling row for categories
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()) // Enable horizontal scrolling
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between buttons
    ) {
        categories.forEach { category ->
            Button(
                onClick = { onCategorySelected(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (category == selectedCategory) MaterialTheme.colorScheme.primary else Color.LightGray
                ),
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = category)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val bitmap = decodeBase64ToBitmap(product.imageUrl)

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Placeholder",
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = product.description ?: "No description available",
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall
        )
        Text(text = "$${product.price}")

        Button(
            onClick = { onAddToCart(product) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Add to Cart")
        }
    }
}
