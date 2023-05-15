package com.example.sportsahead.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDate(simpleDateFormat: SimpleDateFormat): String {
    return try {
        simpleDateFormat.format(
            Date(this)
        )
    } catch (e: Exception) {
        ""
    }
}