package com.app.activeparks.ui.userProfile.model

/**
 * Created by O.Dziuba on 25.12.2023.
 */
data class VideoCategory(
    val id: Long,
    val title: String
) {

    companion object {
        fun getCategory() = listOf(
            VideoCategory(0, "VideoCategory 1"),
            VideoCategory(1, "VideoCategory 2"),
            VideoCategory(2, "VideoCategory "),
        )
    }
}