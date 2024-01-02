package com.app.activeparks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.activeparks.data.db.converter.ActivityInfoTrainingItemConverter
import com.app.activeparks.data.db.converter.ActivityTimeConverter
import com.app.activeparks.data.db.converter.GeoPointConverters
import com.app.activeparks.data.db.converter.InfoItemsConverter
import com.app.activeparks.data.db.converter.ListListDoubleConverter
import com.app.activeparks.data.db.converter.RegionsConverters
import com.app.activeparks.data.db.converter.UriTypeConverter
import com.app.activeparks.data.db.dao.ActiveDao
import com.app.activeparks.data.db.dao.ActivityPauseDao
import com.app.activeparks.data.db.dao.ActivityStateDao
import com.app.activeparks.data.db.dao.EventStateDao
import com.app.activeparks.data.db.dao.ProfileStateDao
import com.app.activeparks.data.db.dao.UserDao
import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.db.entity.ActivityPauseEntity
import com.app.activeparks.data.db.entity.ActivityStateEntity
import com.app.activeparks.data.db.entity.UserEntity
import com.app.activeparks.data.db.entity.EventStateEntity
import com.app.activeparks.data.db.entity.ProfileStateEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Database(
    entities = [
        ActiveEntity::class,
        ActivityStateEntity::class,
        ActivityPauseEntity::class,
        UserEntity::class,
        EventStateEntity::class,
        ProfileStateEntity::class
    ],
    version = 6, exportSchema = false
)
@TypeConverters(
    GeoPointConverters::class,
    ActivityInfoTrainingItemConverter::class,
    UriTypeConverter::class,
    RegionsConverters::class,
    InfoItemsConverter::class,
    ActivityTimeConverter::class,
    ListListDoubleConverter::class
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun activeDao(): ActiveDao
    abstract fun activeStateDao(): ActivityStateDao
    abstract fun activePauseDao(): ActivityPauseDao
    abstract fun eventStateDao(): EventStateDao
    abstract fun profileStateDao(): ProfileStateDao
    abstract fun userDao(): UserDao
}