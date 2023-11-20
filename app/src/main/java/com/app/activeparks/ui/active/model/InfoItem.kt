package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class InfoItem(
    val id: Int,
    var number: String,
    var unit: String,
    var description: String,
    var img: Int = R.drawable.ic_pulse
) {
    companion object {
        fun getPulseInfo(): List<InfoItem> {
            return listOf(
                InfoItem(
                    0, "75", " уд/хв", "Мінімальний", R.drawable.ic_heartbeat_min
                ),
                InfoItem(
                    1, "75", " уд/хв", "Середній", R.drawable.ic_heartbeat_min
                ),
                InfoItem(
                    2, "175", " уд/хв", "Максимальний", R.drawable.ic_heartbeat_max
                )
            )
        }

        fun pausePulse() = InfoItem(
            10, "75", " уд/хв", "На паузу", R.drawable.ic_heartbeat_min
        )

        fun maxPulse() = InfoItem(
            11, "175", " уд/хв", "Максимальний", R.drawable.ic_heartbeat_max
        )
    }
}

fun InfoItem.getPulseInfo() = "${this.number} ${this.unit}"
