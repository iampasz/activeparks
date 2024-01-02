package com.app.activeparks.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.app.activeparks.ui.userProfile.model.DurationPicker
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import java.util.Calendar

/**
 * Created by O.Dziuba on 26.12.2023.
 */
class MonthYearPickerDialog(
    context: Context,
    private val onDateSetListener: OnDateSetListener,
    private val defaultYear: Int,
    private val defaultMonth: Int,
    private val durationPicker: DurationPicker
) {

    companion object {
        fun formatSelectedDate(year: Int, month: Int): String {
            val monthNames = arrayOf(
                "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
                "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"
            )
            return "${monthNames[month - 1]}, $year"
        }
    }

    private val dialog: AlertDialog


    init {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_month_year_picker, null)

        val weekPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val resultTextView = dialogView.findViewById<TextView>(R.id.resultTextView)

        when(durationPicker) {
            DurationPicker.WEEK -> {
                // Приховати yearPicker і відображати weekContainer для вибору тижня
                yearPicker.gone()
                weekPicker.visible()

                // Ініціалізувати NumberPicker для вибору тижня
                val currentCalendar = Calendar.getInstance()
                val selectedMonth = defaultMonth
                val selectedYear = defaultYear

                // Визначити кількість тижнів у вибраному місяці
                currentCalendar.set(selectedYear, selectedMonth - 1, 1)
                val weeksInMonth = currentCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)

                // Визначити поточний тиждень та тижні на місяць назад
                val currentWeek = 4
                var startWeek = 1
//                startWeek = if (startWeek < 0) 52 - startWeek * (-1) else startWeek


                // Встановити значення для weekPicker та визначити текстові представлення
                weekPicker.minValue = 1
                weekPicker.maxValue = 52 // або 53 в залежності від кількості тижнів у році

                val weekLabels = (1..52).map {
                    val startDate = getStartDateOfWeek(selectedYear, it)
                    val endDate = startDate.clone() as Calendar
                    endDate.add(Calendar.DAY_OF_MONTH, 6) // Додати 6 днів для отримання кінця тижня

                    val startMonth = startDate.get(Calendar.MONTH) + 1
                    val endMonth = endDate.get(Calendar.MONTH) + 1

                    "${String.format("%02d", startDate.get(Calendar.DAY_OF_MONTH))}.$startMonth - ${
                        String.format("%02d", endDate.get(Calendar.DAY_OF_MONTH))
                    }.$endMonth"
                }.toTypedArray()

                weekPicker.displayedValues = weekLabels

                // Змінити слухача для відстеження зміни тижня
                weekPicker.setOnValueChangedListener { _, _, newVal ->
                    // Тут ви можете взяти нове значення тижня та обробити його за потребою
                    // Наприклад, вивести вибраний тиждень у відповідне текстове поле
//                    updateResultWeekText(resultTextView, selectedMonth, selectedYear, newVal)
                }

                builder.setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val selectedMonth = weekPicker.value
                        val selectedYear = yearPicker.value
                        onDateSetListener.onDateSet(selectedMonth, selectedYear)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            }
            DurationPicker.MONTH -> {
                weekPicker.minValue = 1
                weekPicker.maxValue = 12
                weekPicker.displayedValues = arrayOf(
                    "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
                    "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"
                )
                weekPicker.value = defaultMonth

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                yearPicker.minValue = currentYear - 1
                yearPicker.maxValue = currentYear
                yearPicker.value = defaultYear

                updateResultText(resultTextView, weekPicker.value, yearPicker.value)

                weekPicker.setOnValueChangedListener { _, _, newVal ->
                    updateResultText(resultTextView, newVal, yearPicker.value)
                }

                yearPicker.setOnValueChangedListener { _, _, newVal ->
                    updateResultText(resultTextView, weekPicker.value, newVal)
                }

                builder.setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val selectedMonth = weekPicker.value
                        val selectedYear = yearPicker.value
                        onDateSetListener.onDateSet(selectedMonth, selectedYear)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            }
            DurationPicker.YEAR -> {

            }
            DurationPicker.ALL -> {

            }
        }


        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }

    private fun updateResultText(textView: TextView, month: Int, year: Int) {
        val monthName = getMonthName(month)
        textView.text = "$monthName, $year"
    }

    private fun updateResultWeekText(textView: TextView, month: Int, year: Int, week: Int = 0) {
        val monthName = getMonthName(month)
        val weekText = if (week > 0) ", Тиждень $week" else ""
        textView.text = "$monthName, $year$weekText"
    }
    private fun getStartDateOfWeek(year: Int, week: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.WEEK_OF_YEAR, week)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek) // Встановлення на перший день тижня
        return calendar
    }

    private fun getMonthName(month: Int): String {
        val monthNames = arrayOf(
            "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
            "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"
        )
        return monthNames[month - 1]
    }

    interface OnDateSetListener {
        fun onDateSet(month: Int, year: Int)
    }
}