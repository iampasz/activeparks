package com.app.activeparks.data.useCase.eventState

import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.ui.active.model.EventState


interface EventStateUseCase {
    suspend fun saveEventState(eventState: EventState)
    suspend fun getEventState(): EventState?
    suspend fun deleteEventState(eventState: EventState)
    suspend fun getEvents(): ListItemEventResponse?
    suspend fun getAdminEvents(): EventResponse?
    suspend fun createEmptyEvent(): ItemEvent?
    suspend fun setDataEvent(id:String, itemEvent: ItemEvent): Boolean?
    suspend fun getEventsForDate(startData:String, endData: String): EventResponse?
    suspend fun getEventDetails(id:String): ItemEvent?

}