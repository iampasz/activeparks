package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityInfoItem(
    val id: Int,
    val title: String,
    val img: Int,
    var isSelected: Boolean,
    val isOutside: Boolean
) {
    companion object {
        fun getActivityInfoItem(): List<ActivityInfoItem> {
            return listOf(
                ActivityInfoItem(0,"Швидкість<br>(км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(1,"Середня швидкість<br>(км/год)", R.drawable.ic_average_speed,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(2,"Макс. швидкість<br>(км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(3,"Дистанція<br>(км)", R.drawable.ic_distance,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(4,"Калорії<br>(ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(5,"Пульс поточний<br>(уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(6,"Пульс середній<br>(уд/хв)", R.drawable.ic_avr_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(7,"Пульс максимальний<br>(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(8,"Зона пульсу", R.drawable.ic_activity,
                    isSelected = false,
                    isOutside = false
                ),
                ActivityInfoItem(9,"середній темп<br>(хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isOutside = true
                ),
                ActivityInfoItem(10,"Макс. висота<br>(м)", R.drawable.ic_hight_up,
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
