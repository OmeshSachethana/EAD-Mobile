package com.example.ecommerce.data.model

import java.util.*

data class Order(
    val id: String? = null,              // Unique ID for the order (nullable for orders that haven't been created yet)
    val customerId: String,              // References the Customer making the order
    val products: List<OrderProduct>,    // List of products in the order
    val status: OrderStatus,             // Order status (Processing, Shipped, Delivered, etc.)
    val notes: String? = null,           // Any additional notes for the order
    val createdAt: Date = Date(),        // When the order was created
    val updatedAt: Date? = null,         // When the order was last updated
    val dispatchedAt: Date? = null,      // When the order was dispatched
    val deliveredAt: Date? = null,       // When the order was delivered
    val isCancelled: Boolean = false,    // Tracks if the order was canceled
    val isPayed: Boolean = false,        // Tracks if the order is paid
    val cancellationNote: String? = null, // Reason for cancellation provided by CSR/Admin
    val isPartiallyDelivered: Boolean = false  // Whether the order is partially delivered
)

data class OrderProduct(
    val productId: String,  // Product ID
    val vendorId: String,   // Vendor ID associated with the product
    val quantity: Int,      // Quantity of the product
    val status: ProductStatus  // Status of the product (Pending, Delivered, etc.)
)

enum class OrderStatus {
    Processing,
    Shipped,
    PartiallyDelivered,
    Delivered,
    Cancelled
}

enum class ProductStatus {
    Pending,            // Product is not yet delivered
    PartiallyDelivered, // Part of the order has been delivered
    Delivered,          // Product has been fully delivered
    Cancelled
}