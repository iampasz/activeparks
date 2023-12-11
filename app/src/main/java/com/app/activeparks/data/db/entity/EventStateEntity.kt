package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_state_table")
data class EventStateEntity (
    @PrimaryKey(autoGenerate = false)
    var keyId: Long = 0,
    @ColumnInfo(name = "event_type")
    val eventType: Int = 0
)
