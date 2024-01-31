package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.events.ImageLinkResponse
import com.app.activeparks.data.model.gallery.PhotoGalleryResponse
import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
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

    @GET("/api/v1/track?sort[updateUp]=asc")
    suspend fun getTracks(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ListTrackResponse>

    @GET("/api/v1/track?sort[updateUp]=asc")
    suspend fun getTracks(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("filters[all]") search: String
    ): Response<ListTrackResponse>

    @POST("/api/v1/track")
    suspend fun createTrack(
    ): Response<TrackResponse>

    @GET("/api/v1/track/{id}")
    suspend fun getTrack(
        @Path("id") id: String,
    ): Response<TrackResponse>

    @GET("/api/v1/track/{id}")
    suspend fun saveTrack(
        @Path("id") id: String,
        @Body request: TrackResponse,
    ): Response<TrackResponse>

    @DELETE("/api/v1/track/{id}")
    suspend fun removeTrack(
        @Path("id") id: String,
    ): Response<TrackResponse>

    @GET("/api/v1/active-routes?sort[updateUp]=asc")
    suspend fun getRouteActives(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ListRouteActiveResponse>

    @GET("/api/v1/active-routes?sort[updateUp]=asc")
    suspend fun getRouteActives(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("filters[all]") search: String
    ): Response<ListRouteActiveResponse>

    @POST("/api/v1/active-routes")
    suspend fun createRouteActive(
        @Path("id") id: String
    ): Response<RouteActiveResponse>

    @GET("/api/v1/active-routes/{id}")
    suspend fun getRouteActive(
        @Path("id") id: String,
    ): Response<RouteActiveResponse>

    @GET("/api/v1/active-routes/{id}")
    suspend fun saveRouteActive(
        @Path("id") id: String,
        @Body request: RouteActiveResponse,
    ): Response<RouteActiveResponse>

    @DELETE("/api/v1/active-routes/{id}")
    suspend fun removeRouteActive(
        @Path("id") id: String,
    ): Response<RouteActiveResponse>


    //Upload file
    @POST("/api/v1/uploads")
    @Multipart
    suspend fun uploadFile(
        @Part("fileName") fileName: String?,
        @Part("chunkIndex") chunkIndex: Int,
        @Part("totalChunk") totalChunk: Int,
        @Part("size") size: Int,
        @Part file: MultipartBody.Part,
        @Part("uploadId") uploadId: String?,
        @Part("videoId") videoId: String?,
        @Part("resolution") resolution: String?,
        @Part("uploadType") uploadType: RequestBody?,
        @Part("sportEventsId") sportEventsId: RequestBody?

    ): Response<ImageLinkResponse>


    //Gallery
    @GET("/api/v1/gallery/{id}/official?")
    suspend fun getPhotoGalleryOfficial(
        @Path("id") id: String
    ): Response<PhotoGalleryResponse>

    //my user, token for test
    //1f17d9c3-0bd1-4423-8731-5df8c8f978fa
    @GET("/api/v1/gallery/{id}/user?")
    suspend fun getPhotoGalleryUser(
        @Path("id") id: String
    ): Response<PhotoGalleryResponse>

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

    @GET("/api/v1/news?offset=0&limit=10&sort[sort_name]=value&filters[filter_name]=value")
    suspend fun getNews(): Response<NewsListResponse>

    @GET("/api/v1/news/{id}")
    suspend fun getNewsDetails(
        @Path("id") id: String?
    ): Response<ItemNews>

    @GET("/api/v1/clubs/{club}/news/{id}")
    suspend fun getClubNewsDetails(
        @Path("club") club_id: String?,
        @Path("id") id: String?
    ): Response<ItemNews>


    //Clubs
    @GET("/api/v1/clubs?offset=0")
    suspend fun getClubList(): Response<ClubListResponse>

    @GET("/api/v1/clubs/my?offset=0&limit=5")
    suspend fun getCombinatedClubList(): Response<ClubsCombinedResponse>

}