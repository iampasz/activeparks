package com.app.activeparks.ui.registration

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.AdditionData
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.SendCodeEmailRequest
import com.app.activeparks.data.model.registration.SendCodePhoneRequest
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

    private val heartRateConst = 220

    var sendSmsCodeLD = MutableLiveData(false)
    var sendEmailCodeLD = MutableLiveData(false)
    var verificationSmsCodeLD = MutableLiveData(false)
    var verificationEmailCodeLD = MutableLiveData(false)
    var onRegistered = MutableLiveData(false)

    val sendCodePhoneRequest = SendCodePhoneRequest()
    val additionData = AdditionData()
    var code = ""
    var email = ""

    fun sendCodePhone() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.sendCodePhone(sendCodePhoneRequest)
            }.onSuccess {
                sendSmsCodeLD.value = true
            }.onFailure { }
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
                        sendCodePhoneRequest.password
                    )
                )
            }.onSuccess {
                verificationSmsCodeLD.value = true
            }.onFailure { }
        }
    }

    fun sendCodeEmail() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.sendCodeEmail(SendCodeEmailRequest(email))
            }.onSuccess {
                sendEmailCodeLD.value = true
            }.onFailure { }
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
            }.onSuccess {
                kotlin.runCatching {
                    userUseCase.insertUser(it.user)
                }.onSuccess {
                    verificationEmailCodeLD.value = true
                }.onFailure { }
            }.onFailure { }
        }
    }

    fun calculatePulseZone(age: Int) {
        val maxHeartRate = heartRateConst - age
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.heartRateZones(
                    PulseZoneRequest(
                        (maxHeartRate * 0.6).toInt(),
                        (maxHeartRate * 0.7).toInt(),
                        (maxHeartRate * 0.8).toInt(),
                        (maxHeartRate * 0.9).toInt(),
                        maxHeartRate
                    )
                )
            }.onSuccess { }.onFailure { }
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                kotlin.runCatching {
                    it?.id?.let { id ->
                        userUseCase.updateData(id, additionData)
                    }
                }.onSuccess {
                    onRegistered.value = true
                }.onFailure { }
            }.onFailure { }
        }
    }
}