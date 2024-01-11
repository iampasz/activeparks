package com.app.activeparks.data.model.sportevents

import com.google.gson.annotations.SerializedName

data class ListItemEventResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("items")
	val items: List<ItemResponse>
)
