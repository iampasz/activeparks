package com.app.activeparks.data.repository.eventState

import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.ui.active.model.EventState


interface EventStateRepository {
    suspend fun saveEventState(eventState: EventState)
    suspend fun getEventState(): EventState?
    suspend fun deleteEventState(eventState: EventState)

    suspend fun getEvents(): ListItemEventResponse?
}