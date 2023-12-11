package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class ResponseToken(
	@SerializedName("payload")
	val payload: Payload,
	@SerializedName("token")
	val token: String,
	@SerializedName("refreshToken")
	val refreshToken: String
)