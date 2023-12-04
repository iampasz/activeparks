package com.app.activeparks.data.network

import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.weather.WeatherResponse
import com.app.activeparks.data.network.baseNew.ApiWithAuthorization
import com.app.activeparks.data.network.baseNew.ApiWithOutAuthorization
import com.app.activeparks.data.network.weather.ApiWeather

/**
 * Created by O.Dziuba on 27.11.2023.
 */
class NetworkManagerImpl(
    private val weather: ApiWeather,
    private val apiWithAuthorization: ApiWithAuthorization,
    private val apiWithOutAuthorization: ApiWithOutAuthorization
) : NetworkManager {

    //Weather
    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return weather.getWeather(lat, lon)
    }

    //WithOut Authorization
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess {
        return apiWithOutAuthorization.sendCodePhone(request)
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken {
        return apiWithOutAuthorization.verificationPhoneCode(request)
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess {
        return apiWithOutAuthorization.sendCodeEmail(request)
    }


    //With Authorization
    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse {
        return apiWithAuthorization.verificationEmailCode(request)
    }

    override suspend fun updateData(id: String, request: AdditionData): User {
        return apiWithAuthorization.updateData(id, request)
    }

    override suspend fun heartRateZones(request: PulseZoneRequest): ResponseSuccess {
        return apiWithAuthorization.heartRateZones(request)
    }


}