package com.app.activeparks.data.useCase.weatehr

import com.app.activeparks.data.model.weather.WeatherResponse
import com.app.activeparks.data.repository.weather.WeatherRepository

/**
 * Created by O.Dziuba on 27.11.2023.
 */
class WeatherUseCaseImpl(
    private val weatherRepository: WeatherRepository
) : WeatherUseCase {
    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return weatherRepository.getWeather(lat, lon)
    }
}