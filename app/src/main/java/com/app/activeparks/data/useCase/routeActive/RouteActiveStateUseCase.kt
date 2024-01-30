package com.app.activeparks.data.useCase.routeActive

import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse

interface RouteActiveStateUseCase {
    suspend fun insert(id: String): RouteActiveResponse?
    suspend fun saveRouteActive(id: String, trackState: RouteActiveResponse)
    suspend fun getRouteActive(id: String): RouteActiveResponse?
    suspend fun deleteTrack(id: String)
    suspend fun getRouteActives(name: String): ListRouteActiveResponse?
}