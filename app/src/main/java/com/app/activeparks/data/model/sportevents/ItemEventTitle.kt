package com.app.activeparks.data.model.sportevents

import com.technodreams.activeparks.R

data class ItemEventTitle(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
    var backgroundResource: Int = R.drawable.background_transparent
)
