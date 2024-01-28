package com.app.activeparks.ui.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import java.io.File

class EventRouteViewModel
    (
    private val eventStateUseCase: EventStateUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    //private val scrollState = "scroll"
    private var mapState: IGeoPoint? = null

    private val geoPointsLiveData = MutableLiveData<ArrayList<GeoPoint>>()


    private val itemEvent = MutableLiveData<ItemEvent>()
    val dataEvent: LiveData<ItemEvent> get() = itemEvent

    fun setGeoPoints(geoPoints: ArrayList<GeoPoint>) {
        geoPointsLiveData.value = geoPoints
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


    val newItemEvent = MutableLiveData<ItemEvent>()
    val dataUpdated = MutableLiveData<Boolean>()
    val imageLink = MutableLiveData<String>()

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

    fun setDataEvent(id:String, itemEvent: ItemEvent) {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.setDataEvent(id, itemEvent)
            }.onSuccess { response ->
                response?.let {
                    dataUpdated.value = it
                }
            }
        }
    }

    fun uploadFile(file: File, type: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                uploadFileUseCase.uploadFile(
                    type,
                    file,
                    null
                )
            }.onSuccess { response ->
                response?.let {
                    imageLink.value = it.url
                }
            }
        }
    }
}