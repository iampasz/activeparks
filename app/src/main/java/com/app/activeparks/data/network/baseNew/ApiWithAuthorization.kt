package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface ApiWithAuthorization {

    @POST("/api/v1/auth-new/verify-code-email")
    suspend fun verificationEmailCode(
        @Body request: VerificationCodeEmailRequest
    ): UserResponse

    @PUT("/api/v1/auth-new/{id}")
    suspend fun updateData(
        @Path("id") id: String,
        @Body request: AdditionData
    ): User

    @POST("/api/v1/heart-rate-zones")
    suspend fun heartRateZones(
        @Body request: PulseZoneRequest
    ): ResponseSuccess

    @POST("/api/v1/workouts-activity")
    suspend fun createActivity(
        @Body request: PulseZoneRequest
    ): ResponseSuccess
}