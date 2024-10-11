package com.example.ecommerce.ui.vendor;

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.R
import com.example.ecommerce.data.model.VendorFeedbackRequest
import com.example.ecommerce.data.network.ApiService
import com.example.ecommerce.data.network.RetrofitClient

class VendorFeedbackActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var commentBox: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_feedback)

        // Initialize the views
        ratingBar = findViewById(R.id.ratingBar)
        commentBox = findViewById(R.id.commentBox)
        submitButton = findViewById(R.id.submitButton)

        // Set up the submit button click listener
        submitButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = commentBox.text.toString()

            // Call the API to submit feedback
            submitFeedback(rating, comment)
        }
    }

    private fun submitFeedback(rating: Int, comment: String) {
        val feedback = VendorFeedbackRequest(ranking = rating, comment = comment)
        val apiService = RetrofitClient.create(ApiService::class.java)
        val call = apiService.submitFeedback("67048047126753b466794a1d", feedback)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VendorFeedbackActivity, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@VendorFeedbackActivity, "Submission failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VendorFeedbackActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
