package com.app.activeparks.data.network

import android.content.Context
import android.widget.Toast
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
import com.app.activeparks.data.network.baseNew.ApiWithAuthorization
import com.app.activeparks.data.network.baseNew.ApiWithOutAuthorization
import com.app.activeparks.data.network.response.parseErrorBody
import com.app.activeparks.data.network.weather.ApiWeather

/**
 * Created by O.Dziuba on 27.11.2023.
 */
class NetworkManagerImpl(
    private val weather: ApiWeather,
    private val apiWithAuthorization: ApiWithAuthorization,
    private val apiWithOutAuthorization: ApiWithOutAuthorization,
    private val context: Context
) : NetworkManager {

    //Weather
    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return weather.getWeather(lat, lon)
    }

    //WithOut Authorization
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.sendCodePhone(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken? {

        val response = apiWithOutAuthorization.verificationPhoneCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.sendCodeEmail(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun login(request: LoginRequest): ResponseToken? {
        val response = apiWithOutAuthorization.login(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }
    override suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.forgotPassword(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.verificationCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken? {
        val response = apiWithOutAuthorization.resetPassword(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }


    //With Authorization
    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse? {
        val response = apiWithAuthorization.verificationEmailCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun updateData(id: String, request: AdditionData): User? {
        val response = apiWithAuthorization.updateData(id, request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess? {
        val response = apiWithAuthorization.setHeartRateZones(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getHeartRateZones(): PulseZoneRequest? {
        val response = apiWithAuthorization.getHeartRateZones()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun createActivity(request: ActiveEntity): ActiveEntity? {
        val response = apiWithAuthorization.createActivity(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getUser(id: String): User? {
        val response = apiWithAuthorization.getUser(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }


}