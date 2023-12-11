package com.app.activeparks.data.useCase.weatehr

import com.app.activeparks.data.model.weather.WeatherResponse

/**
 * Created by O.Dziuba on 27.11.2023.
 */
interface WeatherUseCase {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse
}