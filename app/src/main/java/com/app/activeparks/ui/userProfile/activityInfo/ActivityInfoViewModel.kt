package com.app.activeparks.ui.userProfile.activityInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 19.01.2024.
 */
class ActivityInfoViewModel(
    private val activityUseCase: SaveActivityUseCase
): ViewModel() {

    val activity: MutableLiveData<ActivityItemResponse> = MutableLiveData()

    fun getActivity(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                activityUseCase.getWorkoutActivity(id)
            }.onSuccess {
                    activity.value = it
            }.onFailure {
                val s = it
            }
        }

    }
}