package com.app.activeparks.data.network

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
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
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.model.weather.WeatherResponse
import java.io.File

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
    suspend fun getEvents(): ListItemEventResponse?


    //With Authorization
    suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse?
    suspend fun updateData(id: String, request: AdditionData): User?
    suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess?
    suspend fun getHeartRateZones(): PulseZoneRequest?
    suspend fun createActivity(request: AddActivityResponse): ResponseId?
    suspend fun getWorkoutsActivity(): ActivityResponse?
    suspend fun getUser(id: String): User?
    suspend fun getStatistics(from: String, to: String): StatisticResponse?
    suspend fun updateFile(
        type: String,
        file: File
    ): Default?

    suspend fun getPhotoGalleryOfficial(id: String): PhotoGalleryResponse?
    suspend fun getPhotoGalleryUser(id: String): PhotoGalleryResponse?


    suspend fun uploadFile(
        type: String,
        file: File,
        itemCurrentId: String?
    ): ImageLinkResponse?

    suspend fun updateUser(id: String, user: User): User?
    suspend fun getAdminEvents(): EventResponse?
    suspend fun createEmptyEvent(): ItemEvent?
    suspend fun setDataEvent(id:String, itemEvent: ItemEvent): Boolean?

    suspend fun getNews(): NewsListResponse?
    suspend fun getNewsDetails(id: String): ItemNews?
    suspend fun getClubNewsDetails(club:String, id: String): ItemNews?

    //Clubs
    suspend fun getClubList(): ClubListResponse?
    suspend fun getCombinatedClubList(id:String): ClubsCombinedResponse?
}