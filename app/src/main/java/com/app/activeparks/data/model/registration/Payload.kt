package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class Payload(

	@SerializedName("subRole")
	val subRole: Boolean,
	@SerializedName("id")
	val id: String,
	@SerializedName("sessionId")
	val sessionId: String
)