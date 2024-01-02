package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class VerificationCodeEmailRequest(
	@SerializedName("email")
	val email: String,
	@SerializedName("code")
	val code: String,
	@SerializedName("firstName")
	val fName: String,
	@SerializedName("lastName")
	val lName: String
)
