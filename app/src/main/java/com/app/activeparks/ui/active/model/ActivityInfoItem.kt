package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityInfoItem(
    val id: Int,
    val title: String,
    val img: Int,
    val isSelected: Boolean,
    val isOutside: Boolean
) {
    companion object {
        fun getActivityInfoItem(): List<ActivityInfoItem> {
            return listOf(
                ActivityInfoItem(0,"Швидкість (км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(1,"Середня швидкість (км/год)", R.drawable.ic_average_speed,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(2,"Максимальна швидкість (км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(3,"Дистанція (км)", R.drawable.ic_distance,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(4,"Калорії (ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(5,"Пульс поточний (уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(6,"Пульс середній (уд/хв)", R.drawable.ic_avr_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(7,"Пульс максимальний (уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(8,"Зона пульсу", R.drawable.ic_activity,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(9,"середній темп (хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(10,"максимальна висота (м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(11,"Кроки", R.drawable.ic_steps,
                    isSelected = false,
                    isOutside = true
                ),
            )
        }
    }
}
