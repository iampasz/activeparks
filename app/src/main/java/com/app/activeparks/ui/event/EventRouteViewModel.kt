package com.app.activeparks.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class EventRouteViewModel : ViewModel() {

    val geoPointsLiveData = MutableLiveData<ArrayList<GeoPoint>>()

    fun setGeoPoints(geoPoints: ArrayList<GeoPoint>) {
        geoPointsLiveData.value = geoPoints
    }

}