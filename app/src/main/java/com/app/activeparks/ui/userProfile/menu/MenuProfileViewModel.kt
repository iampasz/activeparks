package com.app.activeparks.ui.userProfile.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.registration.UserUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class MenuProfileViewModel(
    private val userUseCase: UserUseCase,
    private val preferences: Preferences,
    private val newRepository: MobileApiSessionRepository,
    private val repository: Repository,
) : ViewModel() {

    val userDate: MutableLiveData<User> = MutableLiveData()
    val logOut: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                userDate.value = it
            }
        }
    }

    fun removeUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.removeUser(preferences.id)
            }.onSuccess {
                logout()
            }
        }
    }

    fun logout() {
        newRepository.clear()
        repository.logout()
        preferences.apply {
            token = null
            id = null
            clear()
        }
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.deleteUser()
            }.onSuccess {
                logOut.value = true
            }
        }
    }
}