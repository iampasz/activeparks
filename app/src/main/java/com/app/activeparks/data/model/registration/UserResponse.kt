package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@SerializedName("user")
	val user: User
)