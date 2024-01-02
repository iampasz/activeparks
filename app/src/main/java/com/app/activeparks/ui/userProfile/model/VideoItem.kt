package com.app.activeparks.ui.userProfile.model

/**
 * Created by O.Dziuba on 22.12.2023.
 */
data class VideoItem(
    var tittle: String,
    var description: String,
    var img: String,
    var videoState: VideoState
) {

    companion object {
        fun getTestVideoItem() = listOf(
            VideoItem(
                "",
                "Робимо вправи з відомим тренером Київщини",
                "https://forbes.ua/static/storage/thumbs/340x340/5/fe/35a76ce2-55434582a47941ea96827d5cd9291fe5.jpg?v=7056_4",
                VideoState.PUBLISHED
            ),VideoItem(
                "",
                "Робимо вправи з відомим тренером Харківщини",
                "https://forbes.ua/static/storage/thumbs/340x340/9/f6/255a331b-1309b3c11f223040f2f5c10c19f30f69.jpg?v=4466_1",
                VideoState.PUBLISHED
            ),VideoItem(
                "",
                "Робимо вправи з відомим тренером Рівненщині",
                "https://culture-rivne.com.ua/img/upload-files/96886820.jpg",
                VideoState.MODERATION
            ),VideoItem(
                "",
                "Робимо вправи з відомим тренером Франківщені",
                "https://sotka.life/wp-content/uploads/2018/12/1-holovna-1.jpg",
                VideoState.DRAFT
            )
        )
    }
}