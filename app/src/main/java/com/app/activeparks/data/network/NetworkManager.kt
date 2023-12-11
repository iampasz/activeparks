package com.app.activeparks.data.network

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.weather.WeatherResponse
import retrofit2.Response

/**
 * Created by O.Dziuba on 27.11.2023.
 */
interface NetworkManager {

    //Weather
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse


    //WithOut Authorization
    suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess?
    suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken?
    suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess?
    suspend fun login(request: LoginRequest): ResponseToken?

    suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess?
    suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess?
    suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken?


    //With Authorization
    suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse?
    suspend fun updateData(id: String, request: AdditionData): User?
    suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess?
    suspend fun getHeartRateZones(): PulseZoneRequest?
    suspend fun createActivity(request: ActiveEntity): ActiveEntity?
    suspend fun getUser(id: String): User?
}