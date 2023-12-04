package com.app.activeparks.data.repository.registratio

import com.app.activeparks.data.db.dao.UserDao
import com.app.activeparks.data.db.mapper.UserMapper
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.ResponseSuccess
import com.app.activeparks.data.model.registration.ResponseToken
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.registration.UserResponse
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.network.NetworkManager

/**
 * Created by O.Dziuba on 28.11.2023.
 */
data class UserRepositoryImpl(
    private val networkManager: NetworkManager,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun sendCodePhone(request: SendCodePhoneRequest): ResponseSuccess {
        return networkManager.sendCodePhone(request)
    }

    override suspend fun verificationPhoneCode(request: VerificationPhoneCode): ResponseToken {
        return networkManager.verificationPhoneCode(request)
    }

    override suspend fun sendCodeEmail(request: SendCodeEmailRequest): ResponseSuccess {
        return networkManager.sendCodeEmail(request)
    }

    override suspend fun verificationEmailCode(request: VerificationCodeEmailRequest): UserResponse {
        return networkManager.verificationEmailCode(request)
    }

    override suspend fun updateData(id: String, request: AdditionData): User {
        return networkManager.updateData(id, request)
    }

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

    override suspend fun heartRateZones(request: PulseZoneRequest): ResponseSuccess {
        return networkManager.heartRateZones(request)
    }

}
