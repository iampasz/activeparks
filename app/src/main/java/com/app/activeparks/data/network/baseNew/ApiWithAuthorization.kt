package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    @GET("/api/v1/workouts-activity/{id}")
    suspend fun getWorkoutActivity(
        @Path("id") id: String
    ): Response<ActivityItemResponse>


    @GET("api/v1/workouts-activity")
    suspend fun getWorkoutsActivity(@Query("sort[createdAt]") sort: String = "asc"): Response<ActivityResponse>
    @GET("api/v1/workouts-activity")
    suspend fun getWorkoutsActivity(
        @Query("filters[startsFrom]") startsFrom: String,
        @Query("filters[startsTo]") startsTo: String,
        @Query("sort[createdAt]") sortOrder: String = "desc"
    ): Response<ActivityResponse>


    @GET("/api/v1/users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<User>

    @DELETE("/api/v1/users/{id}")
    suspend fun removeUser(
        @Path("id") id: String
    ): Response<User>

    @GET("/api/v1/profile-statistics")
    suspend fun getStatistics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("filters[startsFrom]") startsFrom: String,
        @Query("filters[startsTo]") startsTo: String
    ): Response<StatisticResponse>


    @POST("/api/v1/uploads")
    @Multipart
    suspend fun updateFile(
        @Part("fileName") fileName: String?,
        @Part("size") size: Int,
        @Part("chunkIndex") chunkIndex: Int,
        @Part("totalChunk") totalChunk: Int,
        @Part("uploadId") uploadId: String?,
        @Part("uploadType") uploadType: RequestBody?,
        @Part file: MultipartBody.Part,
    ): Response<Default>
    @PUT("/api/v1/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>

    @GET("/api/v1/sport-events/my?offset=0&limit=100&filters[isClubEvent]=0&sort[createdAt]=desc")
    suspend fun getAdminEvents(): Response<EventResponse>

    @POST("/api/v1/sport-events/")
    suspend fun createEmptyEvent(): Response<ItemEvent>

    @PUT("/api/v1/sport-events/{id}")
    fun setDataEvent(
        @Path("id") id: String?,
        @Body itemEvent: ItemEvent?
    ): Call<String>

    @POST("/api/v1/user-videos")
    suspend fun createUserVideo(): Response<UserVideoItem>

    @GET("/api/v1/user-videos/{id}")
    suspend fun getUserVideo(
        @Path("id") id: String
    ): Response<UserVideoItem>

    @GET("/api/v1/user-videos/my?offset=0&limit=30&sort[priority]=desc&sort[updatedAt]=asc")
    suspend fun getUserVideos(): Response<VideosResponse>

    @PUT("/api/v1/user-videos/{id}")
    suspend fun updateUserVideo(
        @Path("id") id: String,
        @Body saveBill: UserVideoItem
    ): Response<Any>

    @POST("/api/v1/user-videos/{id}/send")
    suspend fun sendUserVideo(
        @Path("id") id: String
    ): Response<ResponseBody>

    @DELETE("/api/v1/user-videos/{id}")
    suspend fun deleteUserVideo(
        @Path("id") id: String
    ): Response<ResponseBody>
}