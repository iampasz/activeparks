package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 15.11.2023.
 */
data class StartInfo(
    var startPoint: InfoItem = InfoItem(
        0,
        "",
        "",
        "Точка старту",
        R.drawable.ic_location
    ),
    var weather: InfoItem = InfoItem(
        1,
        "",
        "Сонячна, 18*С",
        "Погода",
        R.drawable.ic_calories
    )
)