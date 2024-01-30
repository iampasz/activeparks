package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.ActivityStateEntity

/**
 * Created by O.Dziuba on 16.11.2023.
 */
@Dao
interface TrackStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrackState(activityState: ActivityStateEntity)
    @Query("SELECT * FROM activity_state_table WHERE keyId = 0")
    suspend fun getTrackState(): ActivityStateEntity?
    @Delete
    suspend fun deleteTrackState(activityState: ActivityStateEntity)
}