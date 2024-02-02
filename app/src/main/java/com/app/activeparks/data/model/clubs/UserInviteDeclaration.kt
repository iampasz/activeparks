package com.app.activeparks.data.model.clubs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInviteDeclaration (

    @SerializedName("userId")
    @Expose
    private val userId: String,

    @SerializedName("id")
    @Expose
    private val id: String? = null,

    @SerializedName("type")
    @Expose
    private val type: String? = null
)


