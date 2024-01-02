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
                    0, "0", " уд/хв", "Мінімальний", R.drawable.ic_heartbeat_min
                ),
                InfoItem(
                    1, "0", " уд/хв", "Середній", R.drawable.ic_heartbeat_min
                ),
                InfoItem(
                    2, "0", " уд/хв", "Максимальний", R.drawable.ic_heartbeat_max
                )
            )
        }

        fun pausePulse(pulse: Int? = null) = InfoItem(
            10, "${if (pulse == null || pulse == 0) 75 else pulse}", " уд/хв", "На паузу", R.drawable.ic_heartbeat_min
        )

        fun maxPulse(pulse: Int? = null) = InfoItem(
            11, "${pulse ?: 0}", " уд/хв", "Максимальний", R.drawable.ic_heartbeat_max
        )
    }
}

fun InfoItem.getPulseInfo() = "${this.number} ${this.unit}"
