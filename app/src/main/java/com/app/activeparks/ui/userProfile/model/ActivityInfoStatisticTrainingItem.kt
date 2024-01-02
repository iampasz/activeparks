package com.app.activeparks.ui.userProfile.model

import com.app.activeparks.util.extention.replaceBrackets
import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityInfoStatisticTrainingItem(
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
        fun getActivityInfoItem(): List<ActivityInfoStatisticTrainingItem> {
            return listOf(
                ActivityInfoStatisticTrainingItem(0,"0","Кількість тренувань", "", R.drawable.ic_medal,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(1,"0","Відвідано заходів", "", R.drawable.ic_home_clabs,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(2,"0","Витрачено калорій", "(ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = false
                ),
                ActivityInfoStatisticTrainingItem(3,"0","Пульс", "(уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoStatisticTrainingItem(4,"0","Пульс мінімальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                ActivityInfoStatisticTrainingItem(5,"0","Трив. активностей", "", R.drawable.ic_alarm,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(6,"0","Дистанція", "(км)", R.drawable.ic_distance,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(7,"0","Середній темп", "(хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(8,"0","Швидкість", "(км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(9,"0","Максимальна швидкість", "км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(10,"0","Максимальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(11,"0","Мінімальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(12,"0","Набір висоти", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                ActivityInfoStatisticTrainingItem(13,"0","Спуск", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
            )
        }
    }
}

fun ActivityInfoStatisticTrainingItem.getUnitInfFirst() = "${this.number} ${this.unit}".replaceBrackets()
fun ActivityInfoStatisticTrainingItem.getInf() = "${this.description} ${this.unit}"
