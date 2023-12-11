package com.app.activeparks.data.useCase.eventState

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
}