package com.example.ecommerce.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
    return try {
        base64String?.let {
            // Remove metadata if it exists (e.g., "data:image/png;base64,")
            val base64Data = it.substringAfter(",")
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    } catch (e: Exception) {
        Log.e("Base64Decode", "Error decoding base64 string: ${e.message}")
        null
    }
}

