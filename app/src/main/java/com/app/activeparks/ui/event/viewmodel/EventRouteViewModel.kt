package com.app.activeparks.ui.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class EventRouteViewModel
    (private val eventStateUseCase: EventStateUseCase) : ViewModel() {

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


    val newItemEvent = MutableLiveData<ItemEvent>()

    fun createEmptyEvent() {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.createEmptyEvent()
            }.onSuccess { response ->
                response?.let {
                    newItemEvent.value = it
                }
            }
        }
    }

    fun updateItemEvent(itemEvent: ItemEvent) {
        viewModelScope.launch {
            kotlin.runCatching {
                    newItemEvent.value = itemEvent
            }.onFailure { throwable ->

            }
        }
    }

}