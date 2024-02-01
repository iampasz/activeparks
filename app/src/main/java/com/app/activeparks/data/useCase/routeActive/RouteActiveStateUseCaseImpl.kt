package com.app.activeparks.data.useCase.routeActive

import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.repository.routeActive.RouteActiveStateRepository

class RouteActiveStateUseCaseImpl (
    val repository: RouteActiveStateRepository
) : RouteActiveStateUseCase {
    override suspend fun insert(id: String): RouteActiveResponse? {
        return repository.insert(id)
    }

    override suspend fun saveRouteActive(id: String, routeActive: RouteActiveResponse) {
        repository.saveRouteActive(id, routeActive)
    }

    override suspend fun getRouteActive(id: String): RouteActiveResponse? {
        return repository.getRouteActive(id)
    }

    override suspend fun removeRouteActives(id: String) {
        repository.removeRouteActives(id)
    }

    override suspend fun getRouteActives(name: String): ListRouteActiveResponse? {
        return repository.getRouteActives(name)
    }

    override suspend fun getFavoriteRouteActive(): ListRouteActiveResponse? {
        return repository.getFavoriteRouteActive()
    }

    override suspend fun addFavoriteRouteActive(id: String): Boolean? {
       return repository.addFavoriteRouteActive(id)
    }

    override suspend fun removeFavoriteRouteActive(id: String): Boolean? {
        return repository.removeFavoriteRouteActive(id)
    }
}