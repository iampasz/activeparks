package com.app.activeparks.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("message")
    val message: String? = ""
)