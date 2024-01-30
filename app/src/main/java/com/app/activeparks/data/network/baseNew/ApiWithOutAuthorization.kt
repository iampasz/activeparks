package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.SimpleLogin
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface ApiWithOutAuthorization {

    @POST("/api/v1/auth-new/send-code-phone")
    suspend fun sendCodePhone(
        @Body request: SendCodePhoneRequest
    ): Response<ResponseSuccess>

    @POST("/api/v1/auth-new/verify-code-phone")
    suspend fun verificationPhoneCode(
        @Body request: VerificationPhoneCode
    ): Response<ResponseToken>

    @POST("/api/v1/auth-new/send-code-email")
    suspend fun sendCodeEmail(
        @Body request: SendCodeEmailRequest
    ): Response<ResponseSuccess>

    @POST(" /api/v1/auth-new//simple-login-using-facebook")
    suspend fun simpleLoginFacebook(
        @Body request: SimpleLogin
    ): Response<ResponseToken>

    @POST(" /api/v1/auth-new/simple-login-using-google")
    suspend fun simpleLoginGoogle(
        @Body request: SimpleLogin
    ): Response<ResponseToken>

    @POST("/api/v1/auth-new/login-new")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ResponseToken>

    @POST("/api/v1/auth-new/change-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ResponseSuccess>

    @POST("/api/v1/auth-new/change-password-verify")
    suspend fun verificationCode(
        @Body request: VerificationCodeForgotPasswordRequest
    ): Response<ResponseSuccess>

    @POST("/api/v1/auth-new/change-password-reset")
    suspend fun resetPassword(
        @Body request: ResetPasswordResponse
    ): Response<ResponseToken>

    @GET("api/v1/sport-events/five-sport-event?&sort[startsAt]=asc")
    suspend fun getEvents(): Response<ListItemEventResponse>

    //Новини
    @GET("/api/v1/news?offset=0&sort[publishedAt]=desc")
    suspend fun getNews(): Response<NewsListResponse>
}