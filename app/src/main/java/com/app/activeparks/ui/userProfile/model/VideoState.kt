package com.app.activeparks.ui.userProfile.model

import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 22.12.2023.
 */
enum class VideoState(val img: Int, val title: String) {
    PUBLISHED(R.drawable.ic_selected, "Опубліковано"),
    MODERATION(R.drawable.ic_loading, "На модерації"),
    DRAFT(R.drawable.ic_draft, "Чернетка")
}