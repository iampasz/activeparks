package com.app.activeparks.ui.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.user.User
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.profileState.ProfileStateUseCase
import com.app.activeparks.ui.active.model.ProfileState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileStateUseCase: ProfileStateUseCase
) : ViewModel() {

    val navigate = MutableLiveData<Fragment?>()

    //var profileDuration = 0

    val sharedPreferences: Preferences? = null

    var user = MutableLiveData<User>()

    var profileState = ProfileState()

    //var profilePulseItems: List<InfoItem> = InfoItem.getPulseInfo()

    //var updateProfileInfoTrainingItem = MutableLiveData(false)
    private val updateUI: MutableLiveData<Boolean> = MutableLiveData(false)
   // val checkLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)
   // val updateMap: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadProfileState()
    }

    private fun loadProfileState() {
        viewModelScope.launch {
            kotlin.runCatching {
                profileStateUseCase.getProfileState()
            }.onSuccess {
                it?.let { profileState = it }
                updateUI.value = true
            }
        }
    }
//
//    fun saveProfileState() {
//        viewModelScope.launch {
//            kotlin.runCatching {
//                profileStateUseCase.saveProfileState(profileState)
//            }
//        }
//    }

//    fun navigateTo(fragment: Fragment) {
//        navigate.value = fragment
//    }

    fun getUser(): LiveData<User?> {
        return user
    }



}