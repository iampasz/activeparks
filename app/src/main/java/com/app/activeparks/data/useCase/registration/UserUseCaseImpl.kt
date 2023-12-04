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
import com.app.activeparks.data.repository.registratio.UserRepository
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.app.activeparks.data.repository.sesion.model.GsonSession

/**
 * Created by O.Dziuba on 28.11.2023.
 */
data class UserUseCaseImpl(
    private val userRepository: UserRepository,
    private val mobileApiSessionRepository: MobileApiSessionRepository
) : UserUseCase {
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess {
        return userRepository.sendCodePhone(request)
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken {
        val response = userRepository.verificationPhoneCode(request)
        mobileApiSessionRepository.save(
            GsonSession(
                response.token, response.refreshToken
            )
        )
        return response
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess {
        return userRepository.sendCodeEmail(request)
    }

    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse {
        return userRepository.verificationEmailCode(request)
    }

    override suspend fun updateData(id: String, request: AdditionData): User {
        return userRepository.updateData(id, request)
    }

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

    override suspend fun heartRateZones(request: PulseZoneRequest): ResponseSuccess {
        return userRepository.heartRateZones(request)
    }

}
