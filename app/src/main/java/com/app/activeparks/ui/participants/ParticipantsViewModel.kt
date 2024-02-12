package com.app.activeparks.ui.participants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.user.UserParticipants
import com.app.activeparks.data.useCase.participants.ParticipantsUseCase
import kotlinx.coroutines.launch

class ParticipantsViewModel(
    private val participantsUseCase : ParticipantsUseCase
) : ViewModel() {


    val getUserApplyingList = MutableLiveData<UserParticipants>()

    //Clubs
    val getUsersListHeadsClub = MutableLiveData<UserParticipants>()
    val getUsersListApplyingClub = MutableLiveData<UserParticipants>()
    val getUsersListMembersClub = MutableLiveData<UserParticipants>()

    //Events
    val getUsersListHeadsEvents = MutableLiveData<UserParticipants>()
    val getUsersListApplyingEvents = MutableLiveData<UserParticipants>()
    val getUsersListMembersEvents = MutableLiveData<UserParticipants>()

    var id = ""
    var isEvent = false

    fun getUserApplying() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getUserApplying(id)
            }.onSuccess { response ->
                response?.let {
                    getUserApplyingList.value = it
                }
            }
        }
    }

    //Clubs
    fun getHeadsClubUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getHeadsClubUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListHeadsClub.value = it
                }
            }
        }
    }

    fun getApplyingClubUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getApplyingClubUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListApplyingClub.value = it
                }
            }
        }
    }

    fun getMembersClubUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getMembersClubUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListMembersClub.value = it
                }
            }
        }
    }

    //Events
    fun getHeadsEventUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getHeadsEventUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListHeadsEvents.value = it
                }
            }
        }
    }

    fun getMembersEventUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getMembersEventUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListMembersEvents.value = it
                }
            }
        }
    }

    fun getApplyingEventUsers() {
        viewModelScope.launch {
            kotlin.runCatching {
                participantsUseCase.getApplyingEventUsers(id)
            }.onSuccess { response ->
                response?.let {
                    getUsersListApplyingEvents.value = it
                }
            }
        }
    }
}