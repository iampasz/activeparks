package com.app.activeparks.data.repository.routeActive

import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.network.NetworkManager
import com.app.activeparks.data.repository.track.TrackStateRepository

class RouteActiveStateRepositoryImpl (
    private val networkManager: NetworkManager
) : RouteActiveStateRepository {
    override suspend fun insert(id: String): RouteActiveResponse? {
        return networkManager.insert(id)
    }

    override suspend fun saveRouteActive(id: String, trackState: RouteActiveResponse): RouteActiveResponse? {
        return networkManager.saveRouteActive(id, trackState)
    }

    override suspend fun getRouteActive(id: String): RouteActiveResponse? {
        return networkManager.getRouteActive(id)
    }

    override suspend fun removeRouteActives(id: String) {
        networkManager.removeRouteActives(id)
    }

    override suspend fun getRouteActives(name: String): ListRouteActiveResponse? {
        return networkManager.getRouteActives(name)
    }
}