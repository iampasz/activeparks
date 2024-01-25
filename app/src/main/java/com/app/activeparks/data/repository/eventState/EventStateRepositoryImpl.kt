package com.app.activeparks.data.repository.eventState

import com.app.activeparks.data.db.dao.EventStateDao
import com.app.activeparks.data.db.mapper.EventStateMapper
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ListItemEventResponse
import com.app.activeparks.data.network.NetworkManager
import com.app.activeparks.ui.active.model.EventState


class EventStateRepositoryImpl(
    private val eventStateDao: EventStateDao,
    private val networkManager: NetworkManager
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

    override suspend fun getEvents(): ListItemEventResponse? {
        return networkManager.getEvents()
    }

    override suspend fun getAdminEvents(): EventResponse? {
        return networkManager.getAdminEvents()
    }

    override suspend fun createEmptyEvent(): ItemEvent? {
        return networkManager.createEmptyEvent()
    }

    override suspend fun setDataEvent(id:String, itemEvent: ItemEvent): Boolean? {
        return networkManager.setDataEvent(id, itemEvent)
    }

}