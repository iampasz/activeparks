package com.app.activeparks.ui.event.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class EventRouteViewModel
    (eventStateUseCase: EventStateUseCase) : ViewModel() {

   // private val savedStateHandle: SavedStateHandle,

    init {
        Log.i("EventRouteViewModel", "Initialized with $eventStateUseCase")
    }



    //private val scrollState = "scroll"
    private var mapState: IGeoPoint? = null

    private val geoPointsLiveData = MutableLiveData<ArrayList<GeoPoint>>()


    private val itemEvent = MutableLiveData<ItemEvent>()
    val dataEvent: LiveData<ItemEvent> get() = itemEvent


//    init {
//       // loadActiveState()
//    }


//    fun loadActiveState() {
//        viewModelScope.launch {
//            kotlin.runCatching {
//                eventStateUseCase.getEventState()
//            }.onSuccess {
//                it?.let { eventState = it }
//                updateUI.value = true
//            }
//        }
//    }


    fun setGeoPoints(geoPoints: ArrayList<GeoPoint>) {
        geoPointsLiveData.value = geoPoints
    }

//    fun saveScrollPosition(scrollY: Int) {
//       // savedStateHandle[scrollState] = scrollY
//    }

  //  fun getScrollPosition(): Int {
       // return savedStateHandle.get<Int>(scrollState) ?: 0
  //  }

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