package com.app.activeparks.data.model.events

data class EventList(
    val items: List<Event>,
    val total: Int,
    val offset: Int,
    val limit: Int
)
