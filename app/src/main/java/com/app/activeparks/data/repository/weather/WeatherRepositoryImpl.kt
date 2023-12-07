package com.app.activeparks.data.repository.weather

import com.app.activeparks.data.model.weather.WeatherResponse
import com.app.activeparks.data.network.NetworkManager

/**
 * Created by O.Dziuba on 27.11.2023.
 */
class WeatherRepositoryImpl(
    private val networkManager: NetworkManager
) : WeatherRepository {
    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return networkManager.getWeather(lat, lon)
    }
}