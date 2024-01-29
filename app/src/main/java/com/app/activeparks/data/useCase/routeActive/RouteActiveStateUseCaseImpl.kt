package com.app.activeparks.data.useCase.routeActive

import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.repository.track.TrackStateRepository
import com.app.activeparks.data.useCase.trackState.TrackStateUseCase

class RouteActiveStateUseCaseImpl (
    val repository: RouteActiveStateUseCase
) : RouteActiveStateUseCase {
    override suspend fun insert(id: String): RouteActiveResponse? {
        return repository.insert(id)
    }

    override suspend fun saveRouteActive(id: String, trackState: RouteActiveResponse) {
        return repository.saveRouteActive(id, trackState)
    }

    override suspend fun getRouteActive(id: String): RouteActiveResponse? {
        return repository.getRouteActive(id)
    }

    override suspend fun deleteTrack(id: String) {
        repository.deleteTrack(id)
    }

    override suspend fun getRouteActives(name: String): ListRouteActiveResponse? {
        return repository.getRouteActives(name)
    }
}