package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class LevelOfActivity(
    val id: Int,
    val title: String,
    val img: Int,
    val description: String,
    var isSelected: Boolean
) {

    companion object {
        fun getLevelOfActivity(): List<LevelOfActivity> {
            return listOf(
                LevelOfActivity(
                    0,
                    "Легка фізична активність",
                    R.drawable.ic_low_level,
                    "Степ-аеробіка, йога, стречінг, танці та подібне",
                    false
                ),
                LevelOfActivity(
                    1,
                    "Мірна фізична активність",
                    R.drawable.ic_medium_level,
                    "Біг на біговій доріжці, вправи з власною вагою, велотренажер та подібне",
                    false
                ),
                LevelOfActivity(
                    2,
                    "Інтенсивна фізична активність",
                    R.drawable.ic_lhigh_level,
                    "Тренування з важкими вантажами, Кросфіт,TRX, єдиноборства та подібне",
                    false
                )
            )
        }
    }
}
