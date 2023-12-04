package com.app.activeparks.data.useCase.registration

import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface UserUseCase {
    suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess
    suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken
    suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess
    suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse
    suspend fun updateData(id: String, request: AdditionData): User

    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
    suspend fun getUser(): User?
    suspend fun heartRateZones(request: PulseZoneRequest): ResponseSuccess
}