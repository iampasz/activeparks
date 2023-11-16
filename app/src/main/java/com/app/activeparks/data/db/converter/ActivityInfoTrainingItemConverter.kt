package com.app.activeparks.data.db.converter
import androidx.room.TypeConverter
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityInfoTrainingItemConverter {

    @TypeConverter
    fun fromString(value: String): List<ActivityInfoTrainingItem> {
        val listType = object : TypeToken<List<ActivityInfoTrainingItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<ActivityInfoTrainingItem>): String {
        return Gson().toJson(value)
    }
}