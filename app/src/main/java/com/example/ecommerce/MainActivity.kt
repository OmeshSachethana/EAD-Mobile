package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecommerce.ui.theme.EcommerceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
<<<<<<< HEAD

        // Retrieve the user information passed from the LoginActivity
        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "User"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "No email"
        val role = intent.getStringExtra(EXTRA_ROLE) ?: "No role"

=======
>>>>>>> 83ff277ff6211c819d2918094b4c975640a91140
        setContent {
            EcommerceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
<<<<<<< HEAD
                        name = username,
                        email = email,
                        role = role,
=======
                        name = "Omesh",
>>>>>>> 83ff277ff6211c819d2918094b4c975640a91140
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
<<<<<<< HEAD

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_ROLE = "EXTRA_ROLE"
    }
}

@Composable
fun Greeting(name: String, email: String, role: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!\nEmail: $email\nRole: $role",
=======
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
>>>>>>> 83ff277ff6211c819d2918094b4c975640a91140
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceTheme {
<<<<<<< HEAD
        Greeting("Android", "android@example.com", "User")
    }
}
=======
        Greeting("Android")
    }
}
>>>>>>> 83ff277ff6211c819d2918094b4c975640a91140
