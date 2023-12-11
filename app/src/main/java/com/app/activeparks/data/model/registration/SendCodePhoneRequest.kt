package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class SendCodePhoneRequest(
	@SerializedName("phone")
	var phone: String = "",
	@SerializedName("nickname")
	var nickname: String = "",
	@SerializedName("password")
	var password: String = ""
)