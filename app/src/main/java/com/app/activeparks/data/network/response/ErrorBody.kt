package com.app.activeparks.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("error")
    val error: String? = ""
)