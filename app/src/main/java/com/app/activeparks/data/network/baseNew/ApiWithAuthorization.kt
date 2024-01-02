package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.statistic.StatisticResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun setHeartRateZones(
        @Body request: PulseZoneRequest
    ): Response<ResponseSuccess>

    @GET("/api/v1/heart-rate-zones")
    suspend fun getHeartRateZones(): Response<PulseZoneRequest>

    @POST("/api/v1/workouts-activity")
    suspend fun createActivity(): Response<ResponseId>

    @PUT("/api/v1/workouts-activity/{id}")
    suspend fun createActivity(
        @Path("id") id: String,
        @Body request: AddActivityResponse
    ): Response<ResponseId>

    @GET("/api/v1/users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<User>

    @GET("/api/v1/profile-statistics")
    suspend fun getStatistics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("filters[startsFrom]") startsFrom: String,
        @Query("filters[startsTo]") startsTo: String
    ): Response<StatisticResponse>
}