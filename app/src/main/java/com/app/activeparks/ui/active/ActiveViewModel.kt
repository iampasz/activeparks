package com.app.activeparks.ui.active

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.InfoItem
import kotlinx.coroutines.launch

class ActiveViewModel(
    private val activityStateUseCase: ActivityStateUseCase
) : ViewModel() {

    val navigate = MutableLiveData<Fragment?>()

    var activityState = ActivityState()
    var activityInfoItems: List<ActivityInfoTrainingItem> = ActivityInfoTrainingItem.getActivityInfoItem()
    var activityPulseItems: List<InfoItem> = InfoItem.getPulseInfo()

    var updateActivityInfoTrainingItem = MutableLiveData(false)
    val updateUI: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateMap: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadActiveState()
    }

    fun loadActiveState() {
        viewModelScope.launch {
            kotlin.runCatching {
                activityStateUseCase.getActivityState()
            }.onSuccess {
                it?.let { activityState = it }
                updateUI.value = true
            }
        }
    }

    fun saveActiveState() {
        viewModelScope.launch {
            kotlin.runCatching {
                activityStateUseCase.saveActivityState(activityState)
            }
        }
    }

    fun navigateTo(fragment: Fragment) {
        navigate.value = fragment
    }
}