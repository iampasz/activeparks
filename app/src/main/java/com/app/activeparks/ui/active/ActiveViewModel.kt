package com.app.activeparks.ui.active

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.activeparks.data.model.activity.GeoPointEntity
import com.app.activeparks.data.model.activity.GeoPointList
import com.app.activeparks.repository.Repository
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.technodreams.activeparks.R
import org.osmdroid.util.GeoPoint
import java.util.stream.Collectors

class ActiveViewModel(application: Application) : AndroidViewModel(application) {
    private val apiRepository = Repository(getApplication<Application>().applicationContext)
    private val _onSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var onSuccess: LiveData<Boolean> = _onSuccess
    private val _listsGeoPoint: MutableLiveData<List<List<GeoPoint>>> = MutableLiveData(emptyList())
    var listsGeoPoint: LiveData<List<List<GeoPoint>>> = _listsGeoPoint

    private val _activityStateLD = MutableLiveData<ActivityState>()
    val activityStateLD: LiveData<ActivityState> get() = _activityStateLD


    var activityState = ActivityState()

    @SuppressLint("CheckResult")
    fun insertGeoPointList(list: List<GeoPoint?>?) {
        apiRepository.insertListGeoPoint(list).subscribe(
            { _onSuccess.setValue(true) }
        ) { _onSuccess.setValue(false) }
    }

    val listGeoPoint: Unit
        @SuppressLint("CheckResult")
        get() {
            apiRepository.activitiesList.subscribe { geoPointLists: List<GeoPointList>, throwable: Throwable? ->
                val list = geoPointLists.stream().map { geoPoints: GeoPointList ->
                    geoPoints.list.stream().map { geoPointEntity: GeoPointEntity ->
                        GeoPoint(
                            geoPointEntity.getaLatitude(),
                            geoPointEntity.getaLongitude()
                        )
                    }
                        .collect(Collectors.toList())
                }
                    .collect(Collectors.toList())
                _listsGeoPoint.postValue(list)
            }
        }
}