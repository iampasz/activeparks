package com.app.activeparks.ui.active.model

import com.app.activeparks.util.extention.replaceBrackets
import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityInfoTrainingItem(
    val id: Int,
    var number: String,
    val description: String,
    val unit: String,
    val img: Int,
    var isSelected: Boolean,
    var isPulseGadget: Boolean,
    val isOutside: Boolean
) {
    companion object {
        fun getActivityInfoItem(): List<ActivityInfoTrainingItem> {
            return listOf(
                ActivityInfoTrainingItem(0,"0","Швидкість", "(км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(1,"0","Середня швидкість", "(км/год)", R.drawable.ic_average_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(2,"0","Максимальна швидкість", "км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(3,"0","Дистанція", "(км)", R.drawable.ic_distance,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(4,"0","Калорії", "(ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(5,"0","Пульс поточний", "(уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(6,"0","Пульс середній", "(уд/хв)", R.drawable.ic_avr_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(7,"0","Пульс максимальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(8,"0","Пульс мінімальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(9,"0","Середній темп", "(хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(10,"0","Максимальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(11,"0","Кроки", " ", R.drawable.ic_steps,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(12,"0","Мінімальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(13,"0","Набір висоти", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(14,"0","Спуск", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                )
            )
        }
        fun getActivityInfoItemSecond(): List<ActivityInfoTrainingItem> {
            return listOf(
                ActivityInfoTrainingItem(0,"0","Швидкість", "(км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(1,"0","Середня швидкість", "(км/год)", R.drawable.ic_average_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(2,"0","Максимальна швидкість", "км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(3,"0","Дистанція", "(км)", R.drawable.ic_distance,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(4,"0","Калорії", "(ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(5,"0","Пульс поточний", "(уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(6,"0","Пульс середній", "(уд/хв)", R.drawable.ic_avr_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(7,"0","Пульс максимальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(8,"0","Пульс мінімальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoTrainingItem(9,"0","Середній темп", "(хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(10,"0","Максимальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(11,"0","Кроки", " ", R.drawable.ic_steps,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(12,"0","Мінімальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(13,"0","Набір висоти", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoTrainingItem(14,"0","Спуск", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                )
            )
        }
    }
}

fun ActivityInfoTrainingItem.getUnitInf() = "${this.number} ${this.unit}".replaceBrackets()
fun ActivityInfoTrainingItem.getInf() = "${this.description} ${this.unit}"
