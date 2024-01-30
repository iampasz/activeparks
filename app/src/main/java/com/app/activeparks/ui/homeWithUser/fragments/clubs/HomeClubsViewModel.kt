package com.app.activeparks.ui.homeWithUser.fragments.clubs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.useCase.clubs.ClubsUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeClubsViewModel(

    private val clubsUseCase: ClubsUseCase
) : ViewModel() {

    val combineClubList = MutableLiveData<ClubsCombinedResponse>()

    fun getCombinatedClubList() {
        viewModelScope.launch {
            kotlin.runCatching {
                clubsUseCase.getCombinatedClubList()
            }.onSuccess { response ->
                response?.let {
                    combineClubList.value = it
                }
            }
        }
    }
}