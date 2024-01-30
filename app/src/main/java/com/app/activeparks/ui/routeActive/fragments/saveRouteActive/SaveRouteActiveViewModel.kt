package com.app.activeparks.ui.routeActive.fragments.saveRouteActive

import androidx.core.app.NotificationCompat.StreamType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.mapper.ActivityStateToActiveEntityMapper
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.routeActive.RouteActiveStateUseCase
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.trackState.TrackStateUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.ActivityTime
import com.app.activeparks.ui.active.model.CurrentActivity
import com.app.activeparks.ui.active.model.StartInfo
import com.app.activeparks.util.AddressOfDoubleUtil
import kotlinx.coroutines.launch
import java.io.File

class SaveRouteActiveViewModel(
    private val trackStateUseCase: RouteActiveStateUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    var trackDate: MutableLiveData<RouteActiveResponse> = MutableLiveData()
    var photos: MutableList<String> = mutableListOf("")

    val back: MutableLiveData<Boolean> = MutableLiveData()

    var isGallery: Boolean = false

    fun saveTrack() {
        viewModelScope.launch {
            kotlin.runCatching {
                trackDate.value?.id?.let {
                    trackStateUseCase.saveRouteActive(it, trackDate.value!!)
                }
            }
        }
    }

    fun saveImage(file: File) {
        viewModelScope.launch {
            kotlin.runCatching {
                file?.let {
                    uploadFileUseCase.updateFile("other_photo", it)
                }
            }.onSuccess { image ->
                image?.url.let {
                    if (isGallery == true){
                        photos.add(it.toString())
                    }else {
                        trackDate.value?.coverImage = it
                }
                }
            }
        }
    }


    fun getTrack(id: String) {
        if (id.isNotEmpty()) {
            viewModelScope.launch {
                kotlin.runCatching {
                    trackStateUseCase.getRouteActive(id)
                }.onSuccess { response ->
                    response?.let { item ->
                        trackDate.value = item
                    }
                }
            }
        }
    }

    fun removeTrack() {
        trackDate.value?.id.let{
        viewModelScope.launch {
            kotlin.runCatching {
                trackStateUseCase.deleteTrack(it!!)
            }.onSuccess { response ->
                back.value = true
            }
        }
        }
    }
}