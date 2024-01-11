package com.app.activeparks.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.app.activeparks.ui.userProfile.model.DurationPicker
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by O.Dziuba on 26.12.2023.
 */
class MonthYearPickerDialog(
    context: Context,
    private val onDateSetListener: OnDateSetListener,
    defaultYear: Int,
    defaultMonth: Int,
    durationPicker: DurationPicker
) {

    companion object {
        fun formatSelectedDate(year: Int, month: Int): String {
            return "${DataHelper.getMonthList()[month - 1]}, $year"
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

        when (durationPicker) {
            DurationPicker.WEEK -> {
                yearPicker.gone()
                weekPicker.visible()

                val currentCalendar = Calendar.getInstance()

                currentCalendar.set(defaultYear, defaultMonth - 1, 1)

                weekPicker.minValue = 0
                weekPicker.maxValue = 3

                val last4WeeksWithYear = getLast4Weeks()
                val (weeks, years) = splitPairsToLists(last4WeeksWithYear)

                weekPicker.displayedValues = weeks.toTypedArray()

                builder.setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val input = weeks[weekPicker.value]

                        onDateSetListener.onWeekSet(input, years[weekPicker.value])
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            }

            DurationPicker.MONTH -> {
                weekPicker.minValue = 1
                weekPicker.maxValue = 12
                weekPicker.displayedValues = DataHelper.getMonthList()
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
                weekPicker.gone()
                resultTextView.gone()

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                yearPicker.minValue = currentYear - 2
                yearPicker.maxValue = currentYear
                yearPicker.value = defaultYear

                builder.setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val selectedYear = yearPicker.value
                        onDateSetListener.onDateSet(1, selectedYear)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            }

            DurationPicker.ALL -> {}
        }


        dialog = builder.create()
    }

    private fun getLast4Weeks(): List<Pair<String, String>> {
        val weeks = mutableListOf<Pair<String, String>>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())

        calendar.firstDayOfWeek = Calendar.MONDAY

        repeat(4) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val startOfWeek = calendar.time
            calendar.add(Calendar.DAY_OF_WEEK, 6)
            val endOfWeek = calendar.time

            weeks.add(
                "${dateFormat.format(startOfWeek)} - ${dateFormat.format(endOfWeek)}" to calendar.get(
                    Calendar.YEAR
                ).toString()
            )

            calendar.add(Calendar.DAY_OF_MONTH, -7)
        }

        return weeks
    }

    private fun splitPairsToLists(pairs: List<Pair<String, String>>): Pair<List<String>, List<String>> {
        val firstList = pairs.map { it.first }
        val secondList = pairs.map { it.second }

        return firstList to secondList
    }

    fun show() {
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateResultText(textView: TextView, month: Int, year: Int) {
        val monthName = DataHelper.getMonthName(month)
        textView.text = "$monthName, $year"
    }

    interface OnDateSetListener {
        fun onDateSet(month: Int, year: Int)
        fun onWeekSet(range: String, year: String)
    }
}