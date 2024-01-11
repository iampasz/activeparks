package com.app.activeparks.ui.active.fragments.saveActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.mapper.ActivityStateToActiveEntityMapper
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.ActivityTime
import com.app.activeparks.ui.active.model.CurrentActivity
import com.app.activeparks.ui.active.model.StartInfo
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 08.11.2023.
 */
class SaveActivityViewModel(
    private val saveActivityUseCase: SaveActivityUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    var currentActivity = CurrentActivity()
    var startInfo = StartInfo()
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)


    fun saveActivity(
        activityState: ActivityState,
        activityInfoItems: List<ActivityInfoTrainingItem>,
        activityTime: ActivityTime
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                currentActivity.file?.let {
                    uploadFileUseCase.updateFile("other_photo", it)
                }
            }.onSuccess {
                kotlin.runCatching {
                    val activity = ActivityStateToActiveEntityMapper.map(
                        startInfo,
                        currentActivity,
                        activityState,
                        activityInfoItems,
                        activityTime,
                        it?.url ?: ""
                    )
                    saveActivityUseCase.insert(activity)
                }.onSuccess {
                    saved.value = true
                }
            }
        }
    }
}