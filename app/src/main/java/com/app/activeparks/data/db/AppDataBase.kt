package com.app.activeparks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.activeparks.data.db.dao.activity.ActiveDao
import com.app.activeparks.data.db.entity.Active

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Database(
    entities = [
        Active::class
    ],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun activeDao(): ActiveDao
}