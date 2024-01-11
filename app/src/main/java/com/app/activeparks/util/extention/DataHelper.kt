package com.app.activeparks.util.extention

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * Created by O.Dziuba on 29.12.2023.
 */
class DataHelper {
    companion object {

        fun getCurrentYear() = Year.now().value.toString()
        fun getMonthFirstDate(month: Int, year: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            val dateFormat = SimpleDateFormat("$year.$month.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun getFirstDayOfYear(): String {
            val currentYear = LocalDate.now().year
            val firstDayOfYear = LocalDate.of(currentYear, 1, 1)
            return firstDayOfYear.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }
        fun getAllTime(): String {
            val firstDayOfYear = LocalDate.of(2022, 1, 1)
            return firstDayOfYear.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }

        fun getLastDayOfYear(): String {
            val currentYear = LocalDate.now().year
            val lastDayOfYear = LocalDate.of(currentYear, 12, 31)
            return lastDayOfYear.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }

        fun getFirstDayOfYear(year: Int): String {
            val firstDayOfYear = LocalDate.of(year, 1, 1)
            return firstDayOfYear.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }

        fun getLastDayOfYear(year: Int): String {
            val lastDayOfYear = LocalDate.of(year, 12, 31)
            return lastDayOfYear.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }

        fun getLastDayOfMonth(year: Int, month: Int): String {
            val yearMonth = YearMonth.of(year, month)
            val lastDay = yearMonth.atEndOfMonth()
            val formatter = DateTimeFormatter.ofPattern("$year.$month.dd")
            return lastDay.format(formatter)
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

        fun getCurrentMontYear(): String {
            val currentYearMonth = YearMonth.now()
            val year = currentYearMonth.year
            val month = currentYearMonth.monthValue
            return "${getMonthName(month)}, $year"
        }
        fun getCurrentWeek(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val startOfWeek = calendar.time
            calendar.add(Calendar.DAY_OF_WEEK, 6)
            val endOfWeek = calendar.time

            return "${dateFormat.format(startOfWeek)} - ${dateFormat.format(endOfWeek)}"
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

        fun formatDateTimeRange(startsAt: String, finishesAt: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startDateTime = LocalDateTime.parse(startsAt, formatter)
            val finishDateTime = LocalDateTime.parse(finishesAt, formatter)

            val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

            val startDate = startDateTime.format(dateFormat)
            val startTime = startDateTime.format(timeFormat)
            val finishTime = finishDateTime.format(timeFormat)

            return "$startDate | $startTime - $finishTime"
        }

        fun isEventInThePast(startsAt: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startDateTime = LocalDateTime.parse(startsAt, formatter)
            val currentDateTime = LocalDateTime.now()

            return startDateTime.isBefore(currentDateTime)
        }


        fun formatBDay(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("uk", "UA"))
            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("uk", "UA"))

            try {
                val date = inputFormat.parse(inputDate)
                return outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        fun getMonthName(month: Int): String {
            return getMonthList()[month - 1]
        }

        fun getMonthList() = arrayOf(
            "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
            "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"
        )
    }
}