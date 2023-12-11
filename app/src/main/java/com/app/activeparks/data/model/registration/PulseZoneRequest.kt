package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class PulseZoneRequest(
	@SerializedName("easy")
	var easy: Int = 0,
	@SerializedName("fatBurning")
	var fatBurning: Int = 0,
	@SerializedName("aerobic")
	var aerobic: Int = 0,
	@SerializedName("anaerobic")
	var anaerobic: Int = 0,
	@SerializedName("upperBorder")
	var upperBorder: Int = 0
)
