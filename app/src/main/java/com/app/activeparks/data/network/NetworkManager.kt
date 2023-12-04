package com.app.activeparks.data.network

import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.weather.WeatherResponse

/**
 * Created by O.Dziuba on 27.11.2023.
 */
interface NetworkManager {

    //Weather
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse


    //WithOut Authorization
    suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess
    suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken
    suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess


    //With Authorization
    suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse
    suspend fun updateData(id: String, request: AdditionData): User
    suspend fun heartRateZones(request: PulseZoneRequest): ResponseSuccess
}