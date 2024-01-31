package com.app.activeparks.data.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.events.ImageLinkResponse
import com.app.activeparks.data.model.gallery.PhotoGalleryResponse
import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.SimpleLogin
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.routeActive.ListRouteActiveResponse
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.model.weather.WeatherResponse
import com.app.activeparks.data.network.baseNew.ApiWithAuthorization
import com.app.activeparks.data.network.baseNew.ApiWithOutAuthorization
import com.app.activeparks.data.network.response.parseErrorBody
import com.app.activeparks.data.network.weather.ApiWeather
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.util.extention.toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken? {

        val response = apiWithOutAuthorization.verificationPhoneCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.sendCodeEmail(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun simpleLoginFacebook(request: SimpleLogin): ResponseToken? {
        val response = apiWithOutAuthorization.simpleLoginFacebook(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun simpleLoginGoogle(request: SimpleLogin): ResponseToken? {
        val response = apiWithOutAuthorization.simpleLoginGoogle(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun login(request: LoginRequest): ResponseToken? {
        val response = apiWithOutAuthorization.login(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.forgotPassword(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess? {
        val response = apiWithOutAuthorization.verificationCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken? {
        val response = apiWithOutAuthorization.resetPassword(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getEvents(): ListItemEventResponse? {
        val response = apiWithOutAuthorization.getEvents()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }


    //With Authorization
    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse? {
        val response = apiWithAuthorization.verificationEmailCode(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun updateData(id: String, request: AdditionData): User? {
        val response = apiWithAuthorization.updateData(id, request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess? {
        val response = apiWithAuthorization.setHeartRateZones(request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getHeartRateZones(): PulseZoneRequest? {
        val response = apiWithAuthorization.getHeartRateZones()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun createActivity(request: AddActivityResponse): ResponseId? {
        val response = apiWithAuthorization.createActivity()

        val body = response.body()
        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        } else {
            body?.id?.let {

                request.id = it
                apiWithAuthorization.createActivity(it, request)
            }
        }

        return body
    }

    override suspend fun updateActivity(id: String, request: AddActivityResponse): ResponseId? {
        val response = apiWithAuthorization.createActivity(id, request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getWorkoutsActivity(): ActivityResponse? {
        val response = apiWithAuthorization.getWorkoutsActivity()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getWorkoutActivity(id: String): ActivityItemResponse? {
        val response = apiWithAuthorization.getWorkoutActivity(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getWorkoutsActivity(
        startsFrom: String,
        startsTo: String
    ): ActivityResponse? {
        val response = apiWithAuthorization.getWorkoutsActivity(startsFrom, startsTo)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getUser(id: String): User? {
        val response = apiWithAuthorization.getUser(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun removeUser(id: String): User? {
        val response = apiWithAuthorization.removeUser(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getAdminEvents(): EventResponse? {

        val response = apiWithAuthorization.getAdminEvents()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }
        return response.body()
    }

    override suspend fun createEmptyEvent(): ItemEvent? {

        val response = apiWithAuthorization.createEmptyEvent()
        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }
        return response.body()
    }

    override suspend fun setDataEvent(id: String, itemEvent: ItemEvent): Boolean {
        return suspendCoroutine { continuation ->
            val call = apiWithAuthorization.setDataEvent(id, itemEvent)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    override suspend fun getClubList(): ClubListResponse? {
        val response = apiWithAuthorization.getClubList()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getCombinatedClubList(): ClubsCombinedResponse? {
        val response = apiWithAuthorization.getCombinatedClubList()
        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }
        return response.body()
    }

    override suspend fun getClubsDetails(id:String): ItemClub? {
        val response = apiWithAuthorization.getClubsDetails(id)
        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }
        return response.body()
    }





    override suspend fun getNews(): NewsListResponse? {

        val response: Response<NewsListResponse>
        val pref = Preferences(context)

        response = if (pref.token.isNullOrEmpty()) {
            apiWithOutAuthorization.getNews()
        } else {
            apiWithAuthorization.getNews()
        }

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getNewsDetails(id: String): ItemNews? {
        val response = apiWithAuthorization.getNewsDetails(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getClubNewsDetails(club: String, id: String): ItemNews? {
        val response = apiWithAuthorization.getClubNewsDetails(club, id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    //Statistics
    override suspend fun getStatistics(from: String, to: String): StatisticResponse? {
        val response = apiWithAuthorization.getStatistics(0, 750, from, to)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun updateFile(
        type: String,
        file: File
    ): Default? {
        val random = (Math.random() * 200).toInt()
        val name = "file$random"

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

        val filename = type.toRequestBody("text/plain".toMediaTypeOrNull())

        val response =
            apiWithAuthorization.updateFile(name, file.length().toInt(), 1, 1, name, filename, body)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    //Gallery

    override suspend fun getPhotoGalleryOfficial(id: String): PhotoGalleryResponse? {
        val response = apiWithAuthorization.getPhotoGalleryOfficial(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getPhotoGalleryUser(id: String): PhotoGalleryResponse? {
        val response = apiWithAuthorization.getPhotoGalleryUser(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun updateUser(id: String, user: User): User? {
        val response = apiWithAuthorization.updateUser(id, user)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun createUserVideo(): UserVideoItem? {
        val response = apiWithAuthorization.createUserVideo()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getUserVideo(id: String): UserVideoItem? {
        val response = apiWithAuthorization.getUserVideo(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getUserVideos(): VideosResponse? {
        val response = apiWithAuthorization.getUserVideos()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun updateUserVideo(id: String, userVideoItem: UserVideoItem) {
        val response = apiWithAuthorization.updateUserVideo(id, userVideoItem)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }
    }

    override suspend fun sendUserVideo(id: String): ResponseBody? {
        val response = apiWithAuthorization.sendUserVideo(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun deleteUserVideo(id: String): ResponseBody? {
        val response = apiWithAuthorization.deleteUserVideo(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().message, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getTracks(name: String): ListTrackResponse? {
        val response = if (name.count() > 1) {
            apiWithAuthorization.getTracks(0, 50, name)
        } else {
            apiWithAuthorization.getTracks(0, 50)
        }

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getTrack(id: String): TrackResponse? {
        val response = apiWithAuthorization.getTrack(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun createTrack(): TrackResponse? {
        val response = apiWithAuthorization.createTrack()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun removeTrack(id: String): TrackResponse? {
        val response = apiWithAuthorization.removeTrack(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun saveTrack(id: String, request: TrackResponse): TrackResponse? {
        val response = apiWithAuthorization.saveTrack(id, request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getRouteActives(name: String): ListRouteActiveResponse? {
        val response = if (name.count() > 1) {
            apiWithAuthorization.getRouteActives(0, 50, name)
        } else {
            apiWithAuthorization.getRouteActives(0, 50)
        }

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getRouteActive(id: String): RouteActiveResponse? {
        val response = apiWithAuthorization.getRouteActive(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun insert(id: String): RouteActiveResponse? {
        val response = apiWithAuthorization.createRouteActive(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun removeRouteActives(id: String): RouteActiveResponse? {
        val response = apiWithAuthorization.removeRouteActive(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun saveRouteActive(
        id: String,
        request: RouteActiveResponse
    ): RouteActiveResponse? {
        val response = apiWithAuthorization.saveRouteActive(id, request)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun getFavoriteRouteActive(): ListRouteActiveResponse? {
        val response = apiWithAuthorization.getFavoritesRouteActive()

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun addFavoriteRouteActive(id: String): Boolean?  {
        val response = apiWithAuthorization.addFavoriteRouteActive(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }

    override suspend fun removeFavoriteRouteActive(id: String): Boolean?  {
        val response = apiWithAuthorization.removeFavoriteRouteActive(id)

        if (!response.isSuccessful) {
            Toast.makeText(context, response.parseErrorBody().error, Toast.LENGTH_LONG).show()
        }

        return response.body()
    }


    //Load file
    override suspend fun uploadFile(
        type: String,
        file: File,
        itemCurrentId: String?
    ): ImageLinkResponse? {

        val random = (Math.random() * 200).toInt()
        val name = "file$random"

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val fileType = type.toRequestBody("text/plain".toMediaTypeOrNull())
        val itemCurrentId2 = itemCurrentId?.toRequestBody("text/plain".toMediaTypeOrNull())

        val response =
            apiWithAuthorization.uploadFile(
                name,
                1,
                1,
                file.length().toInt(),
                body,
                name,
                null,
                null,
                fileType,
                itemCurrentId2
            )

        if (!response.isSuccessful) {
            toast(context, response.parseErrorBody().message.toString())
        }

        return response.body()
    }
}