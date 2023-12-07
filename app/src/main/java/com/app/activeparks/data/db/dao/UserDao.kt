package com.app.activeparks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.activeparks.data.db.entity.UserEntity

/**
 * Created by O.Dziuba on 28.11.2023.
 */

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteUser()

    @Query("SELECT * FROM user_table")
    suspend fun getUser(): UserEntity?
}