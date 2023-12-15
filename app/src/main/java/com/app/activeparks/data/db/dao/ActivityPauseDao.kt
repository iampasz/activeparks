package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.ActivityPauseEntity

/**
 * Created by O.Dziuba on 16.11.2023.
 */
@Dao
interface ActivityPauseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActivityState(activityState: ActivityPauseEntity)
    @Query("SELECT * FROM activity_pause_table")
    suspend fun getActivityState(): ActivityPauseEntity?
    @Query("DELETE FROM activity_pause_table")
    suspend fun deleteActivityState()
}