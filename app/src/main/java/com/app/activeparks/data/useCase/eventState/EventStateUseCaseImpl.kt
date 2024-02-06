package com.app.activeparks.data.useCase.eventState

import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.data.repository.eventState.EventStateRepository
import com.app.activeparks.ui.active.model.EventState


class EventStateUseCaseImpl(
    private val repository: EventStateRepository
) : EventStateUseCase {
    override suspend fun saveEventState(eventState: EventState) {
        repository.saveEventState(eventState)
    }


    override suspend fun getEventState(): EventState? {
        return repository.getEventState()
    }

    override suspend fun deleteEventState(eventState: EventState) {
        repository.deleteEventState(eventState)
    }

    override suspend fun getEvents(): ListItemEventResponse? {
        return repository.getEvents()
    }

    override suspend fun getAdminEvents(): EventResponse? {
        return repository.getAdminEvents()
    }

    override suspend fun createEmptyEvent(): ItemEvent? {
        return repository.createEmptyEvent()
    }

    override suspend fun setDataEvent(id:String, itemEvent: ItemEvent): Boolean? {
        return repository.setDataEvent(id, itemEvent)
    }

    override suspend fun getEventsForDate(startData: String, endData: String): EventResponse? {
        return repository.getEventsForDate(startData, endData)
    }


}