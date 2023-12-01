package com.app.activeparks.ui.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.sportevents.ItemEvent
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class EventRouteViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val scrollState = "scroll"
    private var mapState: IGeoPoint? = null

    private val geoPointsLiveData = MutableLiveData<ArrayList<GeoPoint>>()

    private val itemEvent = MutableLiveData<ItemEvent>()
    val dataEvent: LiveData<ItemEvent> get() = itemEvent


    fun setGeoPoints(geoPoints: ArrayList<GeoPoint>) {
        geoPointsLiveData.value = geoPoints
    }

    fun saveScrollPosition(scrollY: Int) {
        savedStateHandle[scrollState] = scrollY
    }

    fun getScrollPosition(): Int {
        return savedStateHandle.get<Int>(scrollState) ?: 0
    }

    fun setLastMapGeoPoint(geoPoint: IGeoPoint) {
        mapState = geoPoint
    }

    fun getLastMapGeoPoint(): IGeoPoint? {
        return mapState
    }

    fun updateItemEventData(newItemEvent: ItemEvent) {
        itemEvent.value = newItemEvent
    }

    fun getGeoPointsLiveData(): LiveData<ArrayList<GeoPoint>> {
        return geoPointsLiveData
    }

}