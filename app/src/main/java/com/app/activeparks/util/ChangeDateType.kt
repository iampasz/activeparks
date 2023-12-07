package com.app.activeparks.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChangeDateType {
    companion object {

        fun formatDateTime(inputDateTime: String): String? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            val date = inputFormat.parse(inputDateTime)
            return date?.let { outputFormat.format(it) }
        }

        fun formatDateTimeReverse(inputDateTime: String): String {
            val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val date = inputFormat.parse(inputDateTime)
            return date?.let { outputFormat.format(it) }!!
        }

        fun formatDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

    }
}