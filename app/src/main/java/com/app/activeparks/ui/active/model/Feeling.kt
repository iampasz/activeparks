package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 08.11.2023.
 */
data class Feeling(
    val id: Int,
    var title: String,
    var img: Int
) {

    companion object {
        fun getFiling() = listOf(
            Feeling(0,"Відмінно", R.drawable.ic_smile),
            Feeling(1,"Чудово", R.drawable.ic_smile),
            Feeling(2,"Добре", R.drawable.ic_smile),
            Feeling(3,"Погано", R.drawable.ic_smile),
            Feeling(4,"Жахливо", R.drawable.ic_smile),
        )
    }
}
