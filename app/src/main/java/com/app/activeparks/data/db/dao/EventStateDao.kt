package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.EventStateEntity

@Dao
interface EventStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEventState(eventState: EventStateEntity)
    @Query("SELECT * FROM event_state_table WHERE keyId = 0")
    suspend fun getEventState(): EventStateEntity?
    @Delete
    suspend fun deleteEventState(eventState: EventStateEntity)
}