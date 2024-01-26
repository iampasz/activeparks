package com.app.activeparks.data.model.gallery

import com.google.gson.annotations.SerializedName

data class PhotoGalleryResponse(
    @SerializedName("items")
    val items: List<PhotoItem>,
    @SerializedName("total")
    val total: Int
)

data class PhotoItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("sportEventsId")
    val sportEventsId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userFirstName")
    val userFirstName: String,
    @SerializedName("userLastName")
    val userLastName: String,
    @SerializedName("userPhoto")
    val userPhoto: String
)