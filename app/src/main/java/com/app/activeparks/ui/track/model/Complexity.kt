package com.app.activeparks.ui.track.model

import com.app.activeparks.data.model.user.UserRole
import com.technodreams.activeparks.R

class Complexity (
    val id: String,
    var title: String,
    var img: Int,
    var background: Int
) {

    companion object {
        fun getComplexity() = listOf(
            Complexity("28e09d0e-jd45-4c6f-a787-3ec7e222f001","Легкий", R.drawable.ic_low_level, R.drawable.background_green_complexity),
            Complexity("28e09d0e-jd45-4c6f-a787-3ec7e222f002","Середній", R.drawable.ic_medium_level, R.drawable.background_yellow),
            Complexity("28e09d0e-jd45-4c6f-a787-3ec7e222f003","Важкий", R.drawable.ic_lhigh_level, R.drawable.background_red)
        )
    }
}