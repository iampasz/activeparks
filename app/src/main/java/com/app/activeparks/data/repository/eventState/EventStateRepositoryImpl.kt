package com.app.activeparks.data.repository.eventState

import com.app.activeparks.data.db.dao.EventStateDao
import com.app.activeparks.data.db.mapper.EventStateMapper
import com.app.activeparks.ui.active.model.EventState


class EventStateRepositoryImpl(
    private val eventStateDao: EventStateDao
) : EventStateRepository {

    override suspend fun saveEventState(eventState: EventState) {
        val entity = EventStateMapper.mapToEntity(eventState)
        eventStateDao.saveEventState(entity)
    }

    override suspend fun getEventState(): EventState? {
        val entity = eventStateDao.getEventState()
        return entity?.let { EventStateMapper.mapToModel(it) }
    }

    override suspend fun deleteEventState(eventState: EventState) {
        val entity = EventStateMapper.mapToEntity(eventState)
        eventStateDao.deleteEventState(entity)
    }
}