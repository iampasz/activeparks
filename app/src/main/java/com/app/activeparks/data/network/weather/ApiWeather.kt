package com.app.activeparks.data.network.weather

import com.app.activeparks.data.model.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by O.Dziuba on 27.11.2023.
 */
interface ApiWeather {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "ua",
        @Query("units") units: String = "metric"
    ): WeatherResponse
}