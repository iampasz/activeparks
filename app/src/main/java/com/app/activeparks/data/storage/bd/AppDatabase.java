package com.app.activeparks.data.storage.bd;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.activeparks.data.model.dictionaries.ExerciseCategory;

@Database(entities = {ExerciseCategory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BdDao bdDao();
}
