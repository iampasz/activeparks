package com.app.activeparks.data.useCase.routeActive

import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse

interface RouteActiveStateUseCase {
    suspend fun insert(id: String): RouteActiveResponse?
    suspend fun saveRouteActive(id: String, trackState: RouteActiveResponse)
    suspend fun getRouteActive(id: String): RouteActiveResponse?
    suspend fun removeRouteActives(id: String)
    suspend fun getRouteActives(name: String): ListRouteActiveResponse?
    suspend fun getFavoriteRouteActive(): ListRouteActiveResponse?
    suspend fun addFavoriteRouteActive(id: String): Boolean?
    suspend fun removeFavoriteRouteActive(id: String): Boolean?
}