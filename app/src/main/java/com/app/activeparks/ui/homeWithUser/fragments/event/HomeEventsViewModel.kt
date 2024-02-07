package com.app.activeparks.ui.homeWithUser.fragments.event

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ItemResponse
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeEventsViewModel(
    private val eventStateUseCase: EventStateUseCase
) : ViewModel() {

    val eventList = MutableLiveData<List<ItemResponse>>()
    val eventDayList = MutableLiveData<EventResponse>()
    val eventData = MutableLiveData<ItemEvent>()

    @SuppressLint("CheckResult")
    fun getEvents() {
//        if (session.load().accessToken().isEmpty()) {
            viewModelScope.launch {
                kotlin.runCatching {
                    eventStateUseCase.getEvents()
                }.onSuccess { response ->
                    response?.let {
                        eventList.value = it.items
                    }
                }
            }
//        }
    }

    @SuppressLint("CheckResult")
    fun getEventsForDate(startData:String, endData:String) {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.getEventsForDate(startData, endData)
            }.onSuccess { response ->
                response?.let {
                    eventDayList.value = it
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getEventDetails(id:String) {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.getEventDetails(id)
            }.onSuccess { response ->
                response?.let {
                    eventData.value = it
                }
            }
        }
    }
}