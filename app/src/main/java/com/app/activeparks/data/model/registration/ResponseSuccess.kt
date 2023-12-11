package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class ResponseSuccess(
	@SerializedName("result")
	val result: Boolean
)