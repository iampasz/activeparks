package com.app.activeparks.ui.active.util

/**
 * Created by O.Dziuba on 17.11.2023.
 */
class CalorieCalculator {
    companion object {
        private fun metForWalk(
            speedKmpH: Double,
            weightKg: Double
        ) = speedKmpH / (3.5 * weightKg / 200)

        private fun metForScandinavianWalk(speedKmpH: Double) = when {
            speedKmpH < 5 -> 4.0
            speedKmpH < 6 -> 5.0
            speedKmpH < 7 -> 6.0
            speedKmpH < 8 -> 7.0
            else -> 8.0
        }

        private fun metForBicycle(speedKmpH: Double) = when (speedKmpH) {
            in 1.0..10.0 -> 4
            in 11.0..20.0 -> 6
            else -> 8
        }

        private fun metForRun(speedKmpH: Double) = when {
            speedKmpH < 8 -> 7.0
            speedKmpH < 10 -> 9.8
            speedKmpH < 12 -> 11.0
            else -> 12.8
        }

        fun calculateCaloriesForWalk(
            durationHours: Int,
            weightKg: Double,
            speedKmpH: Double
        ): Int {
            return (metForWalk(
                speedKmpH,
                weightKg
            ) * secondsToHours(durationHours) * weightKg).toInt()
        }

        fun calculateCaloriesForScandinavianWalk(
            durationHours: Int,
            weightKg: Double,
            speedKmpH: Double
        ): Int {
            return (metForScandinavianWalk(speedKmpH) * secondsToHours(durationHours) * speedKmpH * weightKg).toInt()
        }

        fun calculateCaloriesForBicycle(
            durationHours: Int,
            weightKg: Double,
            speedKmpH: Double
        ): Int {
            return (metForBicycle(speedKmpH) * secondsToHours(durationHours) * weightKg).toInt()
        }

        fun calculateCaloriesForRun(
            durationHours: Int,
            weightKg: Double,
            speedKmpH: Double
        ): Int {
            return (metForRun(speedKmpH) * secondsToHours(durationHours) * weightKg).toInt()
        }

        fun calculateImt(weight: Double?, height: Int?): Double? {
            return if (weight != null && height != null) {
                val hM = height.toDouble() / 100
                weight / (hM * hM)
            } else null
        }

        fun getImtStatus(bmi: Double): String {
            return when {
                bmi < 18.5 -> "Недостатня вага"
                bmi in 18.5..24.9 -> "Нормальна вага"
                bmi in 25.0..29.9 -> "Надлишкова вага (передожиріння)"
                bmi in 30.0..34.9 -> "Ожиріння I ступеня"
                bmi in 35.0..39.9 -> "Ожиріння II ступеня"
                else -> "Ожиріння III ступеня (морбідне ожиріння)"
            }
        }


        private fun secondsToHours(seconds: Int) = seconds / 3600.0
    }
}