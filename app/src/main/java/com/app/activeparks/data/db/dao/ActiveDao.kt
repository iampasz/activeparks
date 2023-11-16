package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.ActiveEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */

@Dao
interface ActiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(active: ActiveEntity)
    @Query("SELECT * FROM active WHERE keyId = :keyId")
    suspend fun getActive(keyId: Long): ActiveEntity
    @Query("SELECT * FROM active")
    suspend fun getActives(): List<ActiveEntity>
}