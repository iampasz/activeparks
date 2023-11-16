package com.app.activeparks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.activeparks.data.db.converter.ActivityInfoTrainingItemConverter
import com.app.activeparks.data.db.converter.GeoPointConverters
import com.app.activeparks.data.db.converter.UriTypeConverter
import com.app.activeparks.data.db.dao.ActiveDao
import com.app.activeparks.data.db.dao.ActivityStateDao
import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.db.entity.ActivityStateEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Database(
    entities = [
        ActiveEntity::class,
        ActivityStateEntity::class
    ],
    version = 1
)
@TypeConverters(
    GeoPointConverters::class,
    ActivityInfoTrainingItemConverter::class,
    UriTypeConverter::class
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun activeDao(): ActiveDao
    abstract fun activeStateDao(): ActivityStateDao
}