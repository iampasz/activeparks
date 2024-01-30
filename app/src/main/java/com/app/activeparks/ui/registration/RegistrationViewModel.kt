package com.app.activeparks.ui.registration

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.LoginRequest
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
import com.app.activeparks.data.model.registration.SimpleLogin
import com.app.activeparks.data.model.registration.VerificationCodeEmailRequest
import com.app.activeparks.data.model.registration.VerificationPhoneCode
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.util.ANDROID_TYPE_ID
import kotlinx.coroutines.launch


/**
 * Created by O.Dziuba on 28.11.2023.
 */
@SuppressLint("StaticFieldLeak")
class RegistrationViewModel(
    private val userUseCase: UserUseCase,
    private val context: Context
) : ViewModel() {

    var sendSmsCodeLD = MutableLiveData(false)
    var sendEmailCodeLD = MutableLiveData(false)
    var verificationSmsCodeLD = MutableLiveData(false)
    var verificationEmailCodeLD = MutableLiveData(false)
    var onRegistered = MutableLiveData(false)
    var onLoggedIn = MutableLiveData(false)
    var onHideProgress = MutableLiveData(false)

    val sendCodePhoneRequest = SendCodePhoneRequest()
    val additionData = AdditionData()
    val loginRequest = LoginRequest()
    var code = ""
    var email = ""
    private var userId = ""

    init {
        clearUser()
    }

    fun sendCodePhone() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.sendCodePhone(sendCodePhoneRequest)
            }.onSuccess { response ->
                response?.let {
                    sendSmsCodeLD.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    @SuppressLint("HardwareIds")
    fun verificationSmsCode() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.verificationPhoneCode(
                    VerificationPhoneCode(
                        sendCodePhoneRequest.phone,
                        code,
                        Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        ),
                        ANDROID_TYPE_ID,
                        sendCodePhoneRequest.nickname,
                        sendCodePhoneRequest.password,
                        additionData.facebookToken,
                        additionData.googleToken
                    )
                )
            }.onSuccess { response ->
                response?.let {
                    userId = it.payload.id
                    verificationSmsCodeLD.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    fun sendCodeEmail() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.sendCodeEmail(SendCodeEmailRequest(email))
            }.onSuccess { response ->
                response?.let {
                    sendEmailCodeLD.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    fun verificationEmailCode() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.verificationEmailCode(
                    VerificationCodeEmailRequest(
                        email,
                        code,
                        additionData.fName,
                        additionData.lName
                    )
                )
            }.onSuccess { response ->
                response?.let {
                    kotlin.runCatching {
                        userUseCase.insertUser(it.user)
                    }.onSuccess {
                        verificationEmailCodeLD.value = true
                    }
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    fun calculatePulseZone(age: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.setHeartRateZones(
                    PulseZoneRequest.getAutoPulseZone(age, 60)
                )
            }.onSuccess { }
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess { response ->
                kotlin.runCatching {
                    userUseCase.updateData(response?.id ?: userId, additionData)
                }.onSuccess { user ->
                    user?.let {
                        onRegistered.value = true
                        kotlin.runCatching {
                            userUseCase.insertUser(it)
                        }
                    } ?: kotlin.run { onHideProgress.value = true }
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.login(loginRequest)
            }.onSuccess { response ->
                response?.let {
                    onLoggedIn.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    fun simpleLoginFacebook() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.simpleLoginFacebook(
                    SimpleLogin(
                        facebookToken = additionData.facebookToken
                    )
                )
            }.onSuccess { response ->
                response?.let {
                    onLoggedIn.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }

    fun simpleLoginGoogle() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.simpleLoginGoogle(
                    SimpleLogin(
                        googleToken = additionData.googleToken
                    )
                )
            }.onSuccess { response ->
                response?.let {
                    onLoggedIn.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }
        }
    }


    private fun clearUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.deleteUser()
            }
        }
    }
}