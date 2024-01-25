package com.app.activeparks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by O.Dziuba on 05.01.2024.
 */
class MainViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {


    var user = MutableLiveData<User>()

    fun updateUserInfo() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()?.let { newUser ->
                    this@MainViewModel.user.value = newUser
                }
            }
        }
    }
}