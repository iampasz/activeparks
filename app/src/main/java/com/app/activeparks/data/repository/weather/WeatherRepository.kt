package com.app.activeparks.data.repository.weather

import com.app.activeparks.data.model.weather.WeatherResponse

/**
 * Created by O.Dziuba on 27.11.2023.
 */
interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse
}