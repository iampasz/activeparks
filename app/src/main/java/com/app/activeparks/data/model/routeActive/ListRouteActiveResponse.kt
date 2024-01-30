package com.app.activeparks.data.model.routeActive

import com.app.activeparks.data.model.track.PointsTrack


data class ListRouteActiveResponse (
    val items: List<RouteActiveResponse>,
    val total: Long,
    val offset: Long,
    val limit: Long
)

data class RouteActiveResponse(
    var address: String? = null,
    val calories: String? = null,
    val complexityId: String? = null,
    var coverImage: String? = null,
    val coverType: List<String>? = null,
    val createdAt: String? = null,
    val createdBy: String? = null,
    val dateFinishRecord: String? = null,
    val dateStartRecord: String? = null,
    var description: String? = null,
    val id: String,
    val integrity: Boolean? = null,
    val isTrackActive: Boolean? = null,
    var name: String? = null,
    val photos: List<String>? = null,
    val pointsTrack: List<PointsTrack>? = null,
    val recommendedTime: String? = null,
    val routeLength: String? = null,
    val screenshot: String? = null,
    val type: String? = null,
    val updatedAt: String? = null
)
