package com.app.activeparks.data.model.uservideo

import com.google.gson.annotations.SerializedName

data class VideosResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("items")
	val items: List<VideoItems>
)

data class VideoItems(

	@field:SerializedName("regions")
	val regions: List<Any?>? = null,

	@field:SerializedName("updatedBy")
	val updatedBy: String? = null,

	@field:SerializedName("exerciseDifficultyLevelId")
	val exerciseDifficultyLevelId: String? = null,

	@field:SerializedName("isLiked")
	val isLiked: Boolean? = null,

	@field:SerializedName("mainPhoto")
	val mainPhoto: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("equipment")
	val equipment: List<Any?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: Boolean? = null,

	@field:SerializedName("resolution")
	val resolution: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("duration")
	val duration: Any? = null,

	@field:SerializedName("sourceUrl")
	val sourceUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("statusId")
	val statusId: String? = null,

	@field:SerializedName("sourceType")
	val sourceType: Any? = null,

	@field:SerializedName("createdBy")
	val createdBy: CreatedBy? = null,

	@field:SerializedName("sourceHost")
	val sourceHost: String? = null,

	@field:SerializedName("subcategoryId")
	val subcategoryId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null,

	@field:SerializedName("likes")
	val likes: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class CreatedBy(

	@field:SerializedName("nickname")
	val nickname: String? = null,

	@field:SerializedName("second_name")
	val secondName: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null
)
