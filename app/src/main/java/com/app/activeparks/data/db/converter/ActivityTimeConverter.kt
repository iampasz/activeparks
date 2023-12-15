package com.app.activeparks.data.db.converter

import androidx.room.TypeConverter
import com.app.activeparks.ui.active.model.ActivityTime
import com.google.gson.Gson

/**
 * Created by O.Dziuba on 15.12.2023.
 */

class ActivityTimeConverter {
    @TypeConverter
    fun fromString(value: String?): ActivityTime {
        return Gson().fromJson(value, ActivityTime::class.java)
    }

    @TypeConverter
    fun toString(activityTime: ActivityTime): String {
        return Gson().toJson(activityTime)
    }
}