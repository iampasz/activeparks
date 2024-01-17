package com.app.activeparks.ui.event.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import kotlinx.coroutines.launch

class MainEventViewModel  (
    private val eventStateUseCase: EventStateUseCase
) : ViewModel() {

    val eventList = MutableLiveData<EventResponse>()

    fun getAdminEvents() {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.getAdminEvents()

            }.onSuccess { response ->
                response?.let {
                    eventList.value = it
                }
            }
        }
    }
}