package com.app.activeparks.ui.active

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.ui.active.model.ActivityState
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class ActiveViewModel(
    val activityUseCase: SaveActivityUseCase
) : ViewModel() {
    private val _onSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var onSuccess: LiveData<Boolean> = _onSuccess
    private val _listsGeoPoint: MutableLiveData<List<List<GeoPoint>>> = MutableLiveData(emptyList())
    var listsGeoPoint: LiveData<List<List<GeoPoint>>> = _listsGeoPoint

    private val _activityStateLD = MutableLiveData<ActivityState>()
    val activityStateLD: LiveData<ActivityState> get() = _activityStateLD

    val navigate = MutableLiveData<Fragment?>()

    var bitmap:Bitmap? = null


    var activityState = ActivityState()
    val updateUI: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLocation: MutableLiveData<Boolean> = MutableLiveData(false)
    val save: MutableLiveData<Boolean> = MutableLiveData(false)
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)


    @SuppressLint("CheckResult")
    fun insertGeoPointList(list: List<GeoPoint?>?) {
//        apiRepository.insertListGeoPoint(list).subscribe(
//            { _onSuccess.setValue(true) }
//        ) { _onSuccess.setValue(false) }
    }

//    val listGeoPoint: Unit
//        @SuppressLint("CheckResult")
//        get() {
//            apiRepository.activitiesList.subscribe { geoPointLists: List<GeoPointList>, throwable: Throwable? ->
//                val list = geoPointLists.stream().map { geoPoints: GeoPointList ->
//                    geoPoints.list.stream().map { geoPointEntity: GeoPointEntity ->
//                        GeoPoint(
//                            geoPointEntity.getaLatitude(),
//                            geoPointEntity.getaLongitude()
//                        )
//                    }
//                        .collect(Collectors.toList())
//                }
//                    .collect(Collectors.toList())
//                _listsGeoPoint.postValue(list)
//            }
//        }

    fun navigateTo(fragment: Fragment) {
        navigate.value = fragment
    }

    fun test() {
        viewModelScope.launch {
            kotlin.runCatching {

            }.onSuccess {

            }.onFailure {
            }
        }
    }
}