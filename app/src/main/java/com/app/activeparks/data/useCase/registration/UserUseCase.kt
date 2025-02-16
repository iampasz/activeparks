package com.app.activeparks.data.useCase.registration

import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
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

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface UserUseCase {
    //Network
    suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess?
    suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken?
    suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess?
    suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse?
    suspend fun updateData(id: String, request: AdditionData): User?
    suspend fun login(request: LoginRequest): ResponseToken?
    suspend fun simpleLoginFacebook(request: SimpleLogin): ResponseToken?
    suspend fun simpleLoginGoogle(request: SimpleLogin): ResponseToken?
    suspend fun getUser(id: String): User?
    suspend fun removeUser(id: String): User?
    suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess?
    suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess?
    suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken?
    suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess?
    suspend fun getHeartRateZones(): PulseZoneRequest?
    suspend fun updateUser(id: String, user: User): User?

    //Dao
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
    suspend fun getUser(): User?
}