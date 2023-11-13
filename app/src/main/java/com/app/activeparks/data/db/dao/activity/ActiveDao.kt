package com.app.activeparks.data.db.dao.activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.activeparks.data.db.entity.Active

/**
 * Created by O.Dziuba on 09.11.2023.
 */

@Dao
interface ActiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(active: Active)
    @Query("SELECT * FROM active WHERE keyId = :keyId")
    suspend fun getActive(keyId: Long): Active
    @Query("SELECT * FROM active")
    suspend fun getActives(): List<Active>
}