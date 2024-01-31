package com.app.activeparks.data.model.routeActive

import com.app.activeparks.data.model.track.PointsTrack

data class ListRouteActiveResponse (
    val items: List<RouteActiveResponse>,
    val total: Long,
    val offset: Long,
    val limit: Long
)

data class RouteActiveResponse(
    var address: String,
    val calories: String,
    var complexityId: String,
    var coverImage: String,
    var coverType: List<String>,
    val createdAt: String,
    val createdBy: String,
    val dateFinishRecord: String,
    val dateStartRecord: String,
    var description: String,
    val id: String,
    var integrity: Boolean,
    val isTrackActive: Boolean,
    var name: String,
    var photos: List<String>,
    val pointsActiveRoutes: List<PointsTrack>,
    var recommendedTime: String,
    val routeLength: String,
    val screenshot: String,
    var type: String,
    var distanceToPoint: Double,
    var location: List<Double>,
    val updatedAt: String
)