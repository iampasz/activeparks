package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class PulseZoneRequest(
	@SerializedName("easy")
	var easy: Int = 114,
	@SerializedName("fatBurning")
	var fatBurning: Int = 133,
	@SerializedName("aerobic")
	var aerobic: Int = 152,
	@SerializedName("anaerobic")
	var anaerobic: Int = 171,
	@SerializedName("upperBorder")
	var upperBorder: Int = 190,
	@SerializedName("pausePulse")
	var pausePulse: Int = 60
) {
	companion object {

		private const val heartRateConst = 220

		fun getAutoPulseZone(age: Int, pauseZone: Int): PulseZoneRequest {
			val maxHeartRate = heartRateConst - age

			return PulseZoneRequest(
				(maxHeartRate * 0.6).toInt(),
				(maxHeartRate * 0.7).toInt(),
				(maxHeartRate * 0.8).toInt(),
				(maxHeartRate * 0.9).toInt(),
				maxHeartRate,
				pauseZone
			)
		}
	}
}
