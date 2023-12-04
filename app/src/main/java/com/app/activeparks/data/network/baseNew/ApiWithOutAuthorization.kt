package com.app.activeparks.data.network.baseNew

import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface ApiWithOutAuthorization {

    @POST("/api/v1/auth-new/send-code-phone")
    suspend fun sendCodePhone(
        @Body request: SendCodePhoneRequest
    ): ResponseSuccess
    @POST("/api/v1/auth-new/verify-code-phone")
    suspend fun verificationPhoneCode(
        @Body request: VerificationPhoneCode
    ): ResponseToken
    @POST("/api/v1/auth-new/send-code-email")
    suspend fun sendCodeEmail(
        @Body request: SendCodeEmailRequest
    ): ResponseSuccess
}