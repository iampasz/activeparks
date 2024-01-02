package com.app.activeparks.ui.active.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class TypeOfActivity(
    val id: Int,
    val title: String,
    val img: Int,
    val isOutside: Boolean,
    var isSelected: Boolean,
    val isInclude: Boolean = false,
    val description: String? = null
) {

    companion object {
        fun getTypeOfActivity(): List<TypeOfActivity> {
            return listOf(
                TypeOfActivity(
                    0, "Ходьба", R.drawable.ic_at_walk,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    1, "Скандинавська ходьба", R.drawable.ic_at_scandinavian_walk,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    2, "Велоспорт", R.drawable.ic_at_bicycle,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    3, "Біг", R.drawable.ic_at_run,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    4, "В приміщенні", R.drawable.ic_at_at_home,
                    isOutside = false,
                    isSelected = false,
                    isInclude = true
                )
            )
        }
        fun getTypeOfActivityWithAll(): List<TypeOfActivity> {
            return listOf(
                TypeOfActivity(
                    0, "Ходьба", R.drawable.ic_at_walk,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    1, "Скандинавська ходьба", R.drawable.ic_at_scandinavian_walk,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    2, "Велоспорт", R.drawable.ic_at_bicycle,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    3, "Біг", R.drawable.ic_at_run,
                    isOutside = true,
                    isSelected = false
                ),
                TypeOfActivity(
                    4, "В приміщенні", R.drawable.ic_at_at_home,
                    isOutside = false,
                    isSelected = false,
                    isInclude = true
                ),
                TypeOfActivity(
                    5, "Всі активності", R.drawable.ic_at_at_home,
                    isOutside = false,
                    isSelected = false,
                    isInclude = true
                )
            )
        }
    }
}
