package com.app.activeparks.ui.event

import com.google.gson.annotations.SerializedName

data class ItemPhotoGallery(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("holdingStatusId")
    val holdingStatusId: String
)