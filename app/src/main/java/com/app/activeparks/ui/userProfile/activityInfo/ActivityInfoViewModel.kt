package com.app.activeparks.ui.userProfile.activityInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by O.Dziuba on 19.01.2024.
 */
class ActivityInfoViewModel(
    private val saveActivityUseCase: SaveActivityUseCase,
    private val activityUseCase: SaveActivityUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    val activity: MutableLiveData<ActivityItemResponse> = MutableLiveData()

    fun getActivity(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                activityUseCase.getWorkoutActivity(id)
            }.onSuccess {
                activity.value = it
            }
        }
    }

    fun updateImg(file: File) {
        viewModelScope.launch {
            kotlin.runCatching {
                uploadFileUseCase.updateFile("other_photo", file)
            }.onSuccess { response ->
                response?.url?.let { url ->
                    kotlin.runCatching {
                        val list = activity.value?.photos?.toMutableList()

                        activity.value?.id?.let { id ->
                            list?.add(url)
                            saveActivityUseCase.updateActivity(
                                id, AddActivityResponse(
                                    photos = list,
                                )
                            )
                        }
                    }.onSuccess {
                        activity.value?.id?.let { id -> getActivity(id) }
                    }
                }
            }
        }
    }
}