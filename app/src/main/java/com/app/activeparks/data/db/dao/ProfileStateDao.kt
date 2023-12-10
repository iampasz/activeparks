package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.ProfileStateEntity

@Dao
interface ProfileStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfileState(profileState: ProfileStateEntity)
    @Query("SELECT * FROM profile_state_table WHERE keyId = 0")
    suspend fun getProfileState(): ProfileStateEntity?
    @Delete
    suspend fun deleteProfileState(profileState: ProfileStateEntity)
}