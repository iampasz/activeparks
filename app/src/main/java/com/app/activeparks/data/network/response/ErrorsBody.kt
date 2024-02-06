package com.app.activeparks.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorsBody(

	@SerializedName("errors")
	val errors: List<ErrorsItem>
)

data class ErrorsItem(

	@SerializedName("msg")
	val msg: String,

	@SerializedName("param")
	val param: String,

	@SerializedName("location")
	val location: String,

	@SerializedName("value")
	val value: String
)
