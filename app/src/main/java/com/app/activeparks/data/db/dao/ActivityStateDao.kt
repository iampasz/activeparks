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
interface ActivityStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActivityState(activityState: ActivityStateEntity)
    @Query("SELECT * FROM activity_state_table WHERE keyId = 0")
    suspend fun getActivityState(): ActivityStateEntity?
    @Delete
    suspend fun deleteActivityState(activityState: ActivityStateEntity)
}