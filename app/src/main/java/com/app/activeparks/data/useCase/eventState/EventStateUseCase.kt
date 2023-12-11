package com.app.activeparks.data.useCase.eventState

import com.app.activeparks.ui.active.model.EventState


interface EventStateUseCase {
    suspend fun saveEventState(eventState: EventState)
    suspend fun getEventState(): EventState?
    suspend fun deleteEventState(eventState: EventState)
}