package com.app.activeparks.data.model.statistic

import com.google.gson.annotations.SerializedName

data class StatisticResponse(
	@SerializedName("data_size")
	val dataSize: Int,
	@SerializedName("temp")
	val temp: Int,
	@SerializedName("distance")
	val distance: Double,
	@SerializedName("calories")
	val calories: Int,
	@SerializedName("speed")
	val speed: Double,
	@SerializedName("altitude_min")
	val altitudeMin: Int,
	@SerializedName("result_min_max")
	val resultMinMax: Int,
	@SerializedName("speed_max")
	val speedMax: Double,
	@SerializedName("hight")
	val hight: Int,
	@SerializedName("time")
	val time: Int,
	@SerializedName("min_bpm")
	val minBpm: Int,
	@SerializedName("bpm")
	val bpm: Int,
	@SerializedName("events")
	val events: Int
)
