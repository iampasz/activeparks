package com.app.activeparks.data.model.sportevents

import com.google.gson.annotations.SerializedName

data class ItemResponse(

	@field:SerializedName("holdingStatusId")
	val holdingStatusId: String,

	@field:SerializedName("routePoints")
	val routePoints: List<RoutePointsItem?>,

	@field:SerializedName("clubId")
	val clubId: Any,

	@field:SerializedName("shortDescription")
	val shortDescription: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("qrCodeEvent")
	val qrCodeEvent: String,

	@field:SerializedName("startAdressPoint")
	val startAdressPoint: String,

	@field:SerializedName("createdBy")
	val createdBy: CreatedBy,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("startsAt")
	val startsAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("finishesAt")
	val finishesAt: String,

	@field:SerializedName("isOrder")
	var isOrder: Int,
	@field:SerializedName("countUser")
	var countUser: Int
)

data class RoutePointsItem(

	@field:SerializedName("point_index")
	val pointIndex: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("time_difference")
	val timeDifference: String,

	@field:SerializedName("type")
	val type: Int,

	@field:SerializedName("deleted_at")
	val deletedAt: Any,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("event_id")
	val eventId: String,

	@field:SerializedName("time_passed")
	val timePassed: Any,

	@field:SerializedName("location")
	val location: List<Any?>,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class CreatedBy(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("nickname")
	val nickname: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("secondName")
	val secondName: String
)
