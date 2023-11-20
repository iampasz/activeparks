package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 03.11.2023.
 */
data class PulseZone(
    val id: Int,
    val title: String,
    val background: Int
) {

    companion object {
        fun getPulseZone(): List<PulseZone> {
            return listOf(
                PulseZone(0,"Максимальний тренінг", R.drawable.view_puls_level_6),
                PulseZone(1,"Анаеробна", R.drawable.view_puls_level_5),
                PulseZone(2,"Аеробна", R.drawable.view_puls_level_4),
                PulseZone(3,"Спалювання жиру", R.drawable.view_puls_level_3),
                PulseZone(4,"Легкий тренінг", R.drawable.view_puls_level_2),
            )
        }
    }
}
