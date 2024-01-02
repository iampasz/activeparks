package com.app.activeparks.util.extention

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by O.Dziuba on 29.12.2023.
 */
class DataHelper {
    companion object {
        fun getMonthFirstDate(month: Int, year: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            val dateFormat = SimpleDateFormat("$year.$month.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
        fun getCurrentMonthFirstDate(): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }


        fun formatDuration(durationInSeconds: Long): String {
            val hours = durationInSeconds / 3600
            val minutes = (durationInSeconds % 3600) / 60
            val seconds = durationInSeconds % 60

            return if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
        fun formatDurationHour(durationInSeconds: Long): String {
            val hours = durationInSeconds / 3600
            val minutes = (durationInSeconds % 3600) / 60
            val seconds = durationInSeconds % 60

            return if (hours > 0) {
                String.format("%02d год %02d хв", hours, minutes)
            } else {
                String.format("%02d хв %02d сек", minutes, seconds)
            }
        }

    }
}