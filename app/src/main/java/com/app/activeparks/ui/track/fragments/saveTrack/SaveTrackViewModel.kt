package com.app.activeparks.ui.track.fragments.saveTrack

import androidx.core.app.NotificationCompat.StreamType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.mapper.ActivityStateToActiveEntityMapper
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.storage.Preferences
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
import org.osmdroid.util.GeoPoint
import java.io.File

class SaveTrackViewModel(
    private val trackStateUseCase: TrackStateUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    val activityRoad: MutableList<GeoPoint> = mutableListOf()

    var trackDate: MutableLiveData<TrackResponse> = MutableLiveData()
    var photos: MutableList<String> = mutableListOf("")

    val back: MutableLiveData<Boolean> = MutableLiveData()

    var isGallery: Boolean = false

    fun saveTrack() {
        viewModelScope.launch {
            kotlin.runCatching {
                trackDate.value?.id?.let {
                    trackStateUseCase.saveTrack(it, trackDate.value!!)
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
                        trackDate.value?.photos =
                            (trackDate.value?.photos.orEmpty() + it.toString()).toMutableList()
                    }else {
                        trackDate.value?.coverImage = it.toString()
                }
                }
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            kotlin.runCatching {
                trackStateUseCase.insert()
            }.onSuccess { response ->
                response?.let { item ->
                    trackDate.value = item
                    val pointsTrackList: List<PointsTrack> = activityRoad.map {
                        PointsTrack(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            turn = ""
                        )
                    }
                    trackDate.value?.pointsTrack = pointsTrackList
                    trackDate.value = trackDate.value
                }
            }
        }
    }

    fun getTrack(id: String) {
        if (id.isNotEmpty()) {
            viewModelScope.launch {
                kotlin.runCatching {
                    trackStateUseCase.getTrack(id)
                }.onSuccess { response ->
                    response?.let { item ->
                        trackDate.value = item
                        item.photos?.toMutableList()?.let {
                            photos = it
                        }
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