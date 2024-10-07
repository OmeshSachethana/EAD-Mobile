package com.example.ecommerce.ui.products

import android.content.Context
import android.content.SharedPreferences
import com.example.ecommerce.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFS_NAME = "shopping_cart_prefs"
private const val CART_KEY_PREFIX = "cart_items_"

fun saveCartItems(context: Context, email: String, cartItems: List<Product>) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val json = gson.toJson(cartItems)
    editor.putString("$CART_KEY_PREFIX$email", json) // Use email as the key
    editor.apply()
}

fun loadCartItems(context: Context, email: String): List<Product> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("$CART_KEY_PREFIX$email", null)
    val type = object : TypeToken<List<Product>>() {}.type
    return if (json != null) {
        gson.fromJson(json, type) ?: emptyList()
    } else {
        emptyList()
    }
}

