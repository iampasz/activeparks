package com.app.activeparks.ui.homeWithUser.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.useCase.registration.UserUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 10.01.2024.
 */
class MainHomeViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()

    fun getUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                user.value = it
            }
        }
    }
}