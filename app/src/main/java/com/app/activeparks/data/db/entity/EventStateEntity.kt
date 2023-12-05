package com.app.activeparks.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "event_state_table")
class EventStateEntity {
    @PrimaryKey(autoGenerate = false)
    var keyId: Long = 0
}