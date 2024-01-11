package com.app.activeparks.ui.userProfile.model

import com.app.activeparks.util.extention.replaceBrackets
import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class PairActivityInfoTrainingItem(
    val id: Int,
    var numberFirst: String,
    var numberSecond: String,
    val description: String,
    val unit: String,
    val img: Int,
    var isSelected: Boolean,
    var isPulseGadget: Boolean,
    val isOutside: Boolean
) {
    companion object {
        fun getPairActivityInfoItem(): List<PairActivityInfoTrainingItem> {
            return listOf(

                PairActivityInfoTrainingItem(0,"0","","Кількість тренувань", "", R.drawable.ic_medal,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(1,"0","","Відвідано заходів", "", R.drawable.ic_home_clubs,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(2,"0","","Витрачено калорій", "(ккал)", R.drawable.ic_calories,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = false
                ),
                PairActivityInfoTrainingItem(3,"0","","Пульс", "(уд/хв)", R.drawable.ic_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                PairActivityInfoTrainingItem(4,"0","","Пульс мінімальний", "(уд/хв)", R.drawable.ic_max_pulse,
                    isSelected = false,
                    isPulseGadget = true,
                    isOutside = false
                ),
                PairActivityInfoTrainingItem(5,"0","","Трив. активностей", "", R.drawable.ic_alarm,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(6,"0","","Дистанція", "(км)", R.drawable.ic_distance,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(7,"0","","Середній темп", "(хв/км)", R.drawable.ic_avr_temp,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(8,"0","","Швидкість", "(км/год)", R.drawable.ic_speed,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(9,"0","","Максимальна швидкість", "км/год)", R.drawable.ic_speed_max,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(10,"0","","Максимальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(11,"0","","Мінімальна висота", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(12,"0","","Набір висоти", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
                PairActivityInfoTrainingItem(13,"0","","Спуск", "(м)", R.drawable.ic_hight_up,
                    isSelected = false,
                    isPulseGadget = false,
                    isOutside = true
                ),
            )
        }
    }
}

fun PairActivityInfoTrainingItem.getUnitInfFirst() = "${this.numberFirst} ${this.unit}".replaceBrackets()
fun PairActivityInfoTrainingItem.getUnitInfSecond() = "${this.numberSecond} ${this.unit}".replaceBrackets()
fun PairActivityInfoTrainingItem.getInf() = "${this.description} ${this.unit}"
