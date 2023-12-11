package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
    ): Response<UserResponse>

    @PUT("/api/v1/auth-new/{id}")
    suspend fun updateData(
        @Path("id") id: String,
        @Body request: AdditionData
    ): Response<User>

    @POST("/api/v1/heart-rate-zones")
    suspend fun heartRateZones(
        @Body request: PulseZoneRequest
    ): Response<ResponseSuccess>

    @POST("/api/v1/workouts-activity")
    suspend fun createActivity(
        @Body request: ActiveEntity
    ): Response<ActiveEntity>

    @GET("/api/v1/users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<User>
}