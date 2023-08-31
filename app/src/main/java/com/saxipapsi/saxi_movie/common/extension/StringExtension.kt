package com.saxipapsi.saxi_movie.common.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toDateFormat(outputFormat: String = "MMM dd, yyyy"): String? {
    return try {
        if (this.trim().isNotEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = dateFormat.parse(this)
            parsedDate?.let { SimpleDateFormat(outputFormat).format(it) }
        } else null
    } catch (e: ParseException) {
        "No date provided"
    }
}

