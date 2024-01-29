package com.app.activeparks.ui.track.fragments.changeMapTrack

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.useCase.trackState.TrackStateUseCase
import kotlinx.coroutines.launch

class TrackChangeViewModel : ViewModel() {

    val trackList = MutableLiveData<List<PointsTrack>>()

}