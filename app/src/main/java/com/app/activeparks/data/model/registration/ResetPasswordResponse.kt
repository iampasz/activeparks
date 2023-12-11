package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
	@SerializedName("phoneLogin")
	var phoneLogin: String = "",
	@SerializedName("password")
	var password: String = "",
	@SerializedName("passwordRepeat")
	var passwordRepeat: String = "",
	@SerializedName("userCode")
	var userCode: String = "",
	@SerializedName("typeId")
	var typeId: Int = 1
)
