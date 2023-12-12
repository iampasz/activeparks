package com.app.activeparks.ui.registration.fragments.forgotPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.ForgotPasswordRequest
import com.app.activeparks.data.model.registration.ResetPasswordResponse
import com.app.activeparks.data.model.registration.VerificationCodeForgotPasswordRequest
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.ui.registration.model.StateForgotPassword
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class ForgotPasswordViewModel(
    private val useCase: UserUseCase
) : ViewModel() {

    val forgotPasswordRequest = ForgotPasswordRequest()
    val verificationCodeForgotPasswordRequest = VerificationCodeForgotPasswordRequest()
    val resetPasswordResponse = ResetPasswordResponse()

    var typeOfForgot = TypeOfForgot.PHONE

    var updateUI = MutableLiveData(false)
    var onHideProgress = MutableLiveData(false)

    val state = StateForgotPassword()

    fun forgotPassword() {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.forgotPassword(forgotPasswordRequest)
            }.onSuccess { response ->
                response?.let {
                    state.isSms = true
                    updateUI.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }.onFailure {
                onHideProgress.value = true
            }
        }
    }

    fun verificationCode() {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.verificationCode(verificationCodeForgotPasswordRequest)
            }.onSuccess { response ->
                response?.let {
                    state.isPassword = true
                    updateUI.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }.onFailure {
                onHideProgress.value = true
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.resetPassword(resetPasswordResponse)
            }.onSuccess { response ->
                response?.let {
                    state.isComplete = true
                    updateUI.value = true
                } ?: kotlin.run { onHideProgress.value = true }
            }.onFailure {
                onHideProgress.value = true
            }
        }
    }
}