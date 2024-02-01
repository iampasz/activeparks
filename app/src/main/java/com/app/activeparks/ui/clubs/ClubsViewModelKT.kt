package com.app.activeparks.ui.clubs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.clubs.UserInviteDeclaration
import com.app.activeparks.data.useCase.clubs.ClubsUseCase
import kotlinx.coroutines.launch

class ClubsViewModelKT(
    private val clubsUseCase: ClubsUseCase
) : ViewModel() {

    val clubList = MutableLiveData<ClubsCombinedResponse>()
    val clubDetails = MutableLiveData<ItemClub>()
    val requestToEntry = MutableLiveData<Boolean>()
    val requestToCansel = MutableLiveData<Boolean>()

    fun getCombinatedClubList() {
        viewModelScope.launch {
            kotlin.runCatching {
                clubsUseCase.getCombinatedClubList()
            }.onSuccess { response ->
                response?.let {
                    clubList.value = it
                }
            }
        }
    }

    fun getClubsDetails(id:String) {
        viewModelScope.launch {
            kotlin.runCatching {
                clubsUseCase.getClubsDetails(id)
            }.onSuccess { response ->
                response?.let {
                    clubDetails.value = it
                }
            }
        }
    }

    fun getApplyUser(id: String, request: UserInviteDeclaration) {
        viewModelScope.launch {
            kotlin.runCatching {
                clubsUseCase.getApplyUser(id, request)
            }.onSuccess { response ->
                response?.let {
                    requestToEntry.value = it
                }
            }
        }
    }

    fun getRejectUser(id: String, request: UserInviteDeclaration) {
        viewModelScope.launch {
            kotlin.runCatching {
                clubsUseCase.getRejectUser(id, request)
            }.onSuccess { response ->
                response?.let {
                    requestToCansel.value = it
                }
            }
        }
    }
}