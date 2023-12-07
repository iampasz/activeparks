package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class VerificationCodeForgotPasswordRequest(
	@SerializedName("phoneLogin")
	var phoneLogin: String = "",
	@SerializedName("userCode")
	var userCode: String = "",
	@SerializedName("typeId")
	var typeId: Int = 1
)
