package com.app.activeparks.ui.userProfile.model

/**
 * Created by O.Dziuba on 25.12.2023.
 */
data class VideoLevel(
    val id: Long,
    val title: String
) {

    companion object {
        fun geLevels() = listOf(
            VideoLevel(0, "VideoLevel 1"),
            VideoLevel(1, "VideoLevel 2"),
            VideoLevel(2, "VideoLevel "),
        )
    }
}