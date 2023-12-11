package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.EventStateEntity
import com.app.activeparks.ui.active.model.EventState


class EventStateMapper {

    companion object {
        fun mapToEntity(eventState: EventState): EventStateEntity {
            return EventStateEntity(
                eventType = eventState.eventType
            )
        }

        fun mapToModel(eventStateEntity: EventStateEntity):EventState {
            return EventState(
                eventType = eventStateEntity.eventType

            )
        }
    }
}