package com.app.activeparks.ui.event.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class EventRouteViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val scrollYKey = "scroll_y"
    val geoPointsLiveData = MutableLiveData<ArrayList<GeoPoint>>()
    private val geoPointLiveData = MutableLiveData<GeoPoint>()
    private var selectedPosition: Int = 0

    private var lastMapGeoPoint: IGeoPoint? = null

    fun setGeoPoint(geoPoint: GeoPoint) {
        geoPointLiveData.value = geoPoint
    }

    fun setGeoPoints(geoPoints: ArrayList<GeoPoint>) {
        geoPointsLiveData.value = geoPoints
    }

    fun saveScrollPosition(scrollY: Int) {
        savedStateHandle[scrollYKey] = scrollY
    }

    fun getScrollPosition(): Int {
        return savedStateHandle.get<Int>(scrollYKey) ?: 0
    }


    fun setLastMapGeoPoint(geoPoint: IGeoPoint) {
        lastMapGeoPoint = geoPoint
    }

    fun getLastMapGeoPoint(): IGeoPoint? {
        return lastMapGeoPoint
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }



}