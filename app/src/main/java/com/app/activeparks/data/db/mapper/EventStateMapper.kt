package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.EventStateEntity
import com.app.activeparks.ui.active.model.EventState


class EventStateMapper {

    companion object {
        fun mapToEntity(eventState: EventState): EventStateEntity {
            return EventStateEntity(

            )
        }

        fun mapToModel(eventStateEntity: EventStateEntity):EventState {
            return EventState(

            )
        }
    }
}