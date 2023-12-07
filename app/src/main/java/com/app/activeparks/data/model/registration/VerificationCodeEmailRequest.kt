package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class VerificationCodeEmailRequest(
	@SerializedName("email")
	val email: String,
	@SerializedName("code")
	val code: String,
	@SerializedName("fName")
	val fName: String,
	@SerializedName("lName")
	val lName: String
)
