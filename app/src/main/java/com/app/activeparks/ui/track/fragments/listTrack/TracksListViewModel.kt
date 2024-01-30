package com.app.activeparks.ui.track.fragments.listTrack

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.trackState.TrackStateUseCase
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.util.extention.toInfo
import kotlinx.coroutines.launch
import org.checkerframework.checker.signature.qual.ClassGetSimpleName
import org.osmdroid.util.GeoPoint


class TracksListViewModel(
    private val trackStateUseCase: TrackStateUseCase
) : ViewModel() {

    val trackList = MutableLiveData<List<TrackResponse>>()
    @SuppressLint("CheckResult")
    fun getTracksList() {
        viewModelScope.launch {
            kotlin.runCatching {
                trackStateUseCase.getTracks("")
            }.onSuccess { response ->
                response?.items?.let { items ->
                    trackList.value = items
                }
            }
        }
    }
    @SuppressLint("CheckResult")
    fun getFindTracksList(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                trackStateUseCase.getTracks(name)
            }.onSuccess { response ->
                response?.items?.let { items ->
                    trackList.value = items
                }
            }
        }
    }
}
