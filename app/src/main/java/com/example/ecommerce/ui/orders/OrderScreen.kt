package com.example.ecommerce.ui.orders

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.ecommerce.data.model.Order
import com.example.ecommerce.data.model.OrderStatus
import com.example.ecommerce.data.network.RetrofitClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun getStatusBadgeColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.Processing -> Color.Yellow
        OrderStatus.Shipped -> Color.Blue
        OrderStatus.PartiallyDelivered -> Color.Cyan
        OrderStatus.Delivered -> Color.Green
        OrderStatus.Cancelled -> Color.Red
    }
}

@Composable
fun OrderScreen(
    token: String,
    orders: List<Order>,
    modifier: Modifier = Modifier,
    onPaymentComplete: () -> Unit // Callback to refresh orders
) {
    val apiService = RetrofitClient.getInstance(token)
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            "Your Orders",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(
                orders,
                key = { order -> order.id ?: "" }
            ) { order ->
                var status by remember { mutableStateOf<OrderStatus?>(null) }

                // Ensure order.id is non-null before using it in the LaunchedEffect
                order.id?.let { orderId ->
                    LaunchedEffect(orderId) {
                        apiService.getOrderStatus(orderId).enqueue(object : Callback<Map<String, Int>> {
                            override fun onResponse(call: Call<Map<String, Int>>, response: Response<Map<String, Int>>) {
                                if (response.isSuccessful) {
                                    val statusValue = response.body()?.get("status") ?: 0
                                    status = OrderStatus.fromInt(statusValue)
                                } else {
                                    Toast.makeText(context, "Failed to get status", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }

                // Card to show order details
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Fix: Use CardDefaults.cardElevation
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Display order ID and other details
                        Text(
                            "Order ID: ${order.id ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Customer ID: ${order.customerId}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            "Ordered on: ${order.createdAt}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            "Notes: ${order.notes ?: "None"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (order.isCancelled) {
                            Text(
                                "Cancellation Reason: ${order.cancellationNote ?: "Not provided"}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                            )
                        }

                        // Spacer to push the badge down to the bottom of the card
                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween // Space them evenly in the row
                        ) {

                            // Status badge at the bottom of the card
                            status?.let {
                                Badge(
                                    containerColor = getStatusBadgeColor(it), // Assign color dynamically
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                ) {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                                    )
                                }
                            } ?: Text(
                                "Loading...",
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            val isPayed: Boolean = order.isPayed

                            if (!order.isCancelled && status == OrderStatus.Shipped) {
                                Button(
                                    onClick = {
                                        // Ensure order ID is not null
                                        order.id?.let { orderId ->
                                            apiService.completePayment(orderId)
                                                .enqueue(object : Callback<Void> {
                                                    override fun onResponse(
                                                        call: Call<Void>,
                                                        response: Response<Void>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            // Handle successful payment completion
                                                            Toast.makeText(
                                                                context,
                                                                "Payment completed for order $orderId",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            onPaymentComplete() // Refresh orders
                                                        } else {
                                                            // Handle failure in completing payment
                                                            Toast.makeText(
                                                                context,
                                                                "Payment failed for order $orderId",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<Void>,
                                                        t: Throwable
                                                    ) {
                                                        // Handle network or other errors
                                                        Toast.makeText(
                                                            context,
                                                            "Error: ${t.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    enabled = !isPayed, // Disable the button if the order is already paid
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isPayed) Color.Gray else MaterialTheme.colorScheme.primary, // Background color
                                        contentColor = if (isPayed) Color.LightGray else Color.White // Text color
                                    )
                                ) {
                                    Text(
                                        text = if (isPayed) "Paid" else "Pay Now"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





