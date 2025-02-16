package com.app.activeparks.ui.userProfile.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.ui.userProfile.model.PhotoType
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class EditProfileViewModel(
private val userUseCase: UserUseCase,
private val uploadFileUseCase: UploadFileUseCase,
private val preferences: Preferences
) : ViewModel() {

    val userDate: MutableLiveData<User> = MutableLiveData()
    val userSaved: MutableLiveData<Boolean> = MutableLiveData(false)
    var user: User? = null

    fun getUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                user = it
                userDate.value = it
            }
        }
    }
    fun updateImg(file: File, photoType: PhotoType) {
        val type = if (photoType == PhotoType.AVATAR) "avatar" else "other_photo"
        viewModelScope.launch {
            kotlin.runCatching {
                uploadFileUseCase.updateFile(type, file)
            }.onSuccess { response ->
                response?.url?.let { url ->
                    userDate.value?.let { user ->
                        when(photoType) {
                            PhotoType.AVATAR -> {
                                userUseCase.updateUser(user.id, user.copy(photo = url))
                            }
                            PhotoType.BACKGROUND -> {
                                userUseCase.updateUser(user.id, user.copy(imageBackground = url))
                            }
                        }
                    }
                }
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

    fun saveUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                user?.let { userUseCase.updateUser(preferences.id, it) }
            }.onSuccess {
                userSaved.value = true
            }
        }
    }
}