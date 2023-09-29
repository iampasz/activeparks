package com.app.activeparks.data.storage.bd;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.activeparks.data.model.activity.GeoPointList;
import com.app.activeparks.data.model.dictionaries.ExerciseCategory;
import com.app.activeparks.util.ListTypeConverter;

@Database(entities = {ExerciseCategory.class, GeoPointList.class}, version = 1, exportSchema = false)
@TypeConverters({ListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BdDao bdDao();

    public abstract ActivityDao activityDao();
}
