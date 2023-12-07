package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class VerificationPhoneCode(
	@SerializedName("phone")
	val phone: String? = null,
	@SerializedName("code")
	val code: String? = null,
	@SerializedName("deviceId")
	val deviceId: String? = null,
	@SerializedName("typeId")
	val typeId: Int? = null,
	@SerializedName("nickname")
	val nickname: String? = null,
	@SerializedName("password")
	val password: String? = null,
	@SerializedName("facebookToken")
	val facebookToken: String? = null,
	@SerializedName("googleToken")
	val googleToken: String? = null,
	@SerializedName("appleToken")
	val appleToken: String? = null,
	@SerializedName("bankIdToken")
	val bankIdToken: String? = null
)