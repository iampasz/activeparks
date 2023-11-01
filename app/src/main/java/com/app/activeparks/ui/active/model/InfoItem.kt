package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class InfoItem(
    val id: Int,
    val title: String,
    val description: String,
    val img: Int
) {
    companion object {
        fun getPulseInfo(): List<InfoItem> {
            return listOf(
                InfoItem(0,"75 уд/хв","Середній", R.drawable.ic_heartbeat_min
                ),
                InfoItem(1,"175 уд/хв","Максимальний", R.drawable.ic_heartbeat_max
                )
            )
        }
    }
}
