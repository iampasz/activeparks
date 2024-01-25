package com.app.activeparks.ui.userProfile.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.ui.userProfile.model.PhotoType
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by O.Dziuba on 10.01.2024.
 */
class ProfileHomeViewModel(
    private val userUseCase: UserUseCase,
    private val preferences: Preferences
) : ViewModel() {

    val userDate: MutableLiveData<User> = MutableLiveData()
    val selectedTub: MutableLiveData<Int> = MutableLiveData()

    fun getUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                userDate.value = it
            }
            kotlin.runCatching {
                userUseCase.getUser(preferences.id)
            }.onSuccess {
                userDate.value = it
            }
        }
    }
}