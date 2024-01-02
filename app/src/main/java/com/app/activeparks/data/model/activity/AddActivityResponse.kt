package com.app.activeparks.data.model.activity

import com.app.activeparks.data.db.entity.ActiveEntity
import com.google.gson.annotations.SerializedName

data class AddActivityResponse(
	@SerializedName("idActiveRoutes")
	val idActiveRoutes: String? = null,
	@SerializedName("routeActivePoints")
	val routeActivePoints: List<List<Double>>? = null,
	@SerializedName("feelingId")
	val feelingId: String? = null,
	@SerializedName("notes")
	val notes: String? = null,
	@SerializedName("bmp")
	val bmp: String? = null,
	@SerializedName("distance")
	val distance: String? = null,
	@SerializedName("isActiveRoute")
	val isActiveRoute: Int? = null,
	@SerializedName("description")
	val description: String? = null,
	@SerializedName("photos")
	val photos: List<String?>? = null,
	@SerializedName("speed")
	val speed: String? = null,
	@SerializedName("maxBmp")
	val maxBmp: String? = null,
	@SerializedName("complexityId")
	val complexityId: String? = null,
	@SerializedName("weather")
	val weather: String? = null,
	@SerializedName("weatherIcon")
	val weatherIcon: String? = null,
	@SerializedName("temperature")
	val temperature: String? = null,
	@SerializedName("startsAt")
	val startsAt: Long? = null,
	@SerializedName("tiltle")
	val tiltle: String? = null,
	@SerializedName("id")
	var id: String? = null,
	@SerializedName("image")
	val image: String? = null,
	@SerializedName("temp")
	val temp: String? = null,
	@SerializedName("address")
	val address: String? = null,
	@SerializedName("idLocal")
	val idLocal: String? = null,
	@SerializedName("speedMax")
	val speedMax: String? = null,
	@SerializedName("minBmp")
	val minBmp: String? = null,
	@SerializedName("routePoints")
	val routePoints: List<List<Double>>? = null,
	@SerializedName("calories")
	val calories: String? = null,
	@SerializedName("hight")
	val hight: String? = null,
	@SerializedName("altitudeMin")
	val altitudeMin: String? = null,
	@SerializedName("time")
	val time: Long? = null,
	@SerializedName("activityType")
	val activityType: String? = null,
	@SerializedName("finishesAt")
	val finishesAt: Long? = null
) {

	companion object {
		fun map(active: ActiveEntity): AddActivityResponse {
			return AddActivityResponse(
				"",
				active.routeActivePoints,
				active.feelingId.toString(),
				active.description,
				active.bpm.toString(),
				active.distance,
				0,
				active.description,
				listOf(),
				active.speed.toString(),
				active.maxBpm.toString(),
				active.complexityId.toString(),
				active.weather,
				active.weatherIcon,
				active.temperature,
				active.startsAt,
				active.title,
				active.idLocal,
				active.coverImage,
				active.temp.toString(),
				active.address,
				active.idLocal,
				active.speedMax.toString(),
				active.minBpm.toString(),
				active.routePoints,
				active.calories.toString(),
				active.height.toString(),
				active.altitudeMin.toString(),
				active.time,
				active.activityType,
				active.finishesAt
			)
		}
	}
}
