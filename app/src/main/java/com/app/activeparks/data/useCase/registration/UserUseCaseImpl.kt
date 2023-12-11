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
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.repository.registratio.UserRepository
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.app.activeparks.data.repository.sesion.model.GsonSession
import com.app.activeparks.data.storage.Preferences

/**
 * Created by O.Dziuba on 28.11.2023.
 */
data class UserUseCaseImpl(
    private val userRepository: UserRepository,
    private val mobileApiSessionRepository: MobileApiSessionRepository,
    private val preferences: Preferences
) : UserUseCase {
    //Network
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess? {
        return userRepository.sendCodePhone(request)
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken? {
        val response = userRepository.verificationPhoneCode(request)
        saveToken(response)
        return response
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess? {
        return userRepository.sendCodeEmail(request)
    }

    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse? {
        return userRepository.verificationEmailCode(request)
    }

    override suspend fun updateData(id: String, request: AdditionData): User? {
        return userRepository.updateData(id, request)
    }

    override suspend fun login(request: LoginRequest): ResponseToken? {
        val response = userRepository.login(request)
        getTokenAndSaveUser(response)

        return response
    }

    private suspend fun getTokenAndSaveUser(response: ResponseToken?) {
        saveToken(response)

        response?.let {
            getUser(it.payload.id)?.let { user -> insertUser(user) }
        }
    }

    override suspend fun getUser(id: String): User? {
        return userRepository.getUser(id)
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess? {
        return userRepository.forgotPassword(request)
    }

    override suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess? {
        return userRepository.verificationCode(request)
    }

    override suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken? {
        val response = userRepository.resetPassword(request)
        getTokenAndSaveUser(response)

        return response
    }

    private fun saveToken(response: ResponseToken?) {
        mobileApiSessionRepository.save(
            GsonSession(
                response?.token ?: "", response?.refreshToken ?: ""
            )
        )
        preferences.token = response?.token ?: ""
    }


    //Dao
    override suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    override suspend fun deleteUser() {
        userRepository.deleteUser()
    }

    override suspend fun getUser(): User? {
        return userRepository.getUser()
    }

    override suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess? {
        return userRepository.setHeartRateZones(request)
    }

    override suspend fun getHeartRateZones(): PulseZoneRequest? {
        return userRepository.getHeartRateZones()
    }

}
