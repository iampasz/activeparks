package com.app.activeparks.data.model.news

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsListResponse(
    @SerializedName("items")
    @Expose
    val items: List<ItemNews>,

    @SerializedName("total")
    @Expose
    val total: Int,

    @SerializedName("offset")
    @Expose
    val offset: Int,

    @SerializedName("limit")
    @Expose
    val limit: Int
)