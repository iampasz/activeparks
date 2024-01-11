package com.app.activeparks.data.model.activity

import com.google.gson.annotations.SerializedName

data class ActivityResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("items")
	val items: List<ActivityItemResponse>
)

data class ActivityItemResponse(

	@field:SerializedName("routeActivePoints")
	val routeActivePoints: List<List<Any?>?>? = null,

	@field:SerializedName("idActiveRoutes")
	val idActiveRoutes: String? = null,

	@field:SerializedName("feelingId")
	val feelingId: Int? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("startsAtDatetime")
	val startsAtDatetime: String? = null,

	@field:SerializedName("weatherIcon")
	val weatherIcon: String? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("isActiveRoute")
	val isActiveRoute: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("photos")
	val photos: List<Any?>? = null,

	@field:SerializedName("speed")
	val speed: Int? = null,

	@field:SerializedName("tempText")
	val tempText: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("complexityId")
	val complexityId: String? = null,

	@field:SerializedName("maxBpm")
	val maxBpm: Int? = null,

	@field:SerializedName("minBpm")
	val minBpm: Int? = null,

	@field:SerializedName("coverImage")
	val coverImage: String,

	@field:SerializedName("weather")
	val weather: String? = null,

	@field:SerializedName("temperature")
	val temperature: String? = null,

	@field:SerializedName("startsAt")
	val startsAt: Long,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("bpm")
	val bpm: Int? = null,

	@field:SerializedName("temp")
	val temp: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("idLocal")
	val idLocal: String? = null,

	@field:SerializedName("speedMax")
	val speedMax: Int? = null,

	@field:SerializedName("routePoints")
	val routePoints: List<List<Any?>?>? = null,

	@field:SerializedName("calories")
	val calories: Int,

	@field:SerializedName("activityUser")
	val activityUser: ActivityUser? = null,

	@field:SerializedName("steps")
	val steps: Int? = null,

	@field:SerializedName("hight")
	val hight: Int? = null,

	@field:SerializedName("time")
	val time: Int? = null,

	@field:SerializedName("altitudeMin")
	val altitudeMin: Int? = null,

	@field:SerializedName("activityType")
	val activityType: String,

	@field:SerializedName("finishesAtDatetime")
	val finishesAtDatetime: String? = null,

	@field:SerializedName("finishesAt")
	val finishesAt: Long
)

data class ActivityUser(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("activity_id")
	val activityId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
