package com.app.activeparks.data.model.activity

import com.google.gson.annotations.SerializedName

/**
 * Created by O.Dziuba on 29.01.2024.
 */
data class PhotoRequest(
    @SerializedName("photos")
    val photos: List<String>? = null,) {
}