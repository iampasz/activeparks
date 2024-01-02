package com.app.activeparks.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.technodreams.activeparks.R
import java.util.Calendar

/**
 * Created by O.Dziuba on 26.12.2023.
 */
class MonthYearPickerDialog(
    context: Context,
    private val onDateSetListener: OnDateSetListener,
    private val defaultYear: Int,
    private val defaultMonth: Int
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

        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val resultTextView = dialogView.findViewById<TextView>(R.id.resultTextView)

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.displayedValues = arrayOf(
            "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
            "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"
        )
        monthPicker.value = defaultMonth

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = currentYear - 1
        yearPicker.maxValue = currentYear
        yearPicker.value = defaultYear

        updateResultText(resultTextView, monthPicker.value, yearPicker.value)

        monthPicker.setOnValueChangedListener { _, _, newVal ->
            updateResultText(resultTextView, newVal, yearPicker.value)
        }

        yearPicker.setOnValueChangedListener { _, _, newVal ->
            updateResultText(resultTextView, monthPicker.value, newVal)
        }

        builder.setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedMonth = monthPicker.value
                val selectedYear = yearPicker.value
                onDateSetListener.onDateSet(selectedMonth, selectedYear)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }

    private fun updateResultText(textView: TextView, month: Int, year: Int) {
        val monthName = getMonthName(month)
        textView.text = "$monthName, $year"
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