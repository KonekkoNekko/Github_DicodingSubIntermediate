package com.dicoding.submissionintermediatebiel

import java.text.SimpleDateFormat
import java.util.Locale

object Util {
    fun formatIso8601Date(iso8601Date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MMMM d, yyyy, 'at' HH:mm", Locale.US)

        return try {
            val date = inputFormat.parse(iso8601Date)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date" // Handle any parsing or formatting errors
        }
    }
}
