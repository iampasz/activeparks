package com.app.activeparks.data.repository.registratio

import com.app.activeparks.data.db.dao.UserDao
import com.app.activeparks.data.db.mapper.UserMapper
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.SimpleLogin
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.network.NetworkManager

/**
 * Created by O.Dziuba on 28.11.2023.
 */
data class UserRepositoryImpl(
    private val networkManager: NetworkManager,
    private val userDao: UserDao
) : UserRepository {
    //Network
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess? {
        return networkManager.sendCodePhone(request)
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken? {
        return networkManager.verificationPhoneCode(request)
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess? {
        return networkManager.sendCodeEmail(request)
    }

    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse? {
        return networkManager.verificationEmailCode(request)
    }

    override suspend fun updateData(id: String, request: AdditionData): User? {
        return networkManager.updateData(id, request)
    }

    override suspend fun login(request: LoginRequest): ResponseToken? {
        return networkManager.login(request)
    }

    override suspend fun simpleLoginFacebook(request: SimpleLogin): ResponseToken? {
        return networkManager.simpleLoginFacebook(request)
    }

    override suspend fun simpleLoginGoogle(request: SimpleLogin): ResponseToken? {
        return networkManager.simpleLoginGoogle(request)
    }

    override suspend fun getUser(id: String): User? {
        return networkManager.getUser(id)
    }

    override suspend fun removeUser(id: String): User? {
        return networkManager.removeUser(id)
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): ResponseSuccess? {
        return networkManager.forgotPassword(request)
    }

    override suspend fun verificationCode(request: VerificationCodeForgotPasswordRequest): ResponseSuccess? {
        return networkManager.verificationCode(request)
    }

    override suspend fun resetPassword(request: ResetPasswordResponse): ResponseToken? {
        return networkManager.resetPassword(request)
    }

    override suspend fun updateUser(id: String, user: User): User? {
        val response = networkManager.updateUser(id, user)
        response?.let {
            updateUser(it)
        }
        return response
    }

    //Dao
    override suspend fun insertUser(user: User) {
        userDao.insertUser(UserMapper.mapToUserEntity(user))
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(UserMapper.mapToUserEntity(user))
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

    override suspend fun getUser(): User? {
        return userDao.getUser()?.let { UserMapper.mapToUser(it) }
    }

    override suspend fun setHeartRateZones(request: PulseZoneRequest): ResponseSuccess? {
        return networkManager.setHeartRateZones(request)
    }

    override suspend fun getHeartRateZones(): PulseZoneRequest? {
        return networkManager.getHeartRateZones()
    }

}
