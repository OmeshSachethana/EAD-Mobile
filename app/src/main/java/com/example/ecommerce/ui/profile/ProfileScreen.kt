package com.example.ecommerce.ui.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    username: String,
    email: String,
    role: String,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit // Add a parameter for the logout callback
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Profile", style = MaterialTheme.typography.titleLarge)
        Text("Username: $username")
        Text("Email: $email")
        Text("Role: $role")
        Button(onClick = { onLogout() }) { // Call the logout function
            Text("Logout")
        }
    }
}
