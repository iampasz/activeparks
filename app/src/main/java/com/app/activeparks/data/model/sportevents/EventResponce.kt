package com.app.activeparks.data.model.sportevents

import com.google.gson.annotations.SerializedName

data class EventResponse (
    @SerializedName("items")
    val items: List<ItemEvent>? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("offset")
    val offset: Int? = null,
    @SerializedName("limit")
    val limit: Int? = null,
)