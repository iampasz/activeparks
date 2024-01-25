package com.app.activeparks.ui.userProfile.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 17.01.2024.
 */
class AllActivitiesViewModel(
    private val saveActivityUseCase: SaveActivityUseCase
) : ViewModel() {

    val activities: MutableLiveData<List<ActivityItemResponse>> = MutableLiveData()

    fun getActivities(
        startsFrom: String,
        startsTo: String
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveActivityUseCase.getWorkoutsActivity(startsFrom, startsTo)
            }.onSuccess {
                it?.let {
                    activities.value = it.items
                }
            }
        }

    }
}