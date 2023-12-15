package com.app.activeparks.data.db.converter

import androidx.room.TypeConverter
import com.app.activeparks.ui.active.model.InfoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by O.Dziuba on 15.12.2023.
 */

class InfoItemsConverter {
    @TypeConverter
    fun fromString(value: String?): List<InfoItem> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<InfoItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<InfoItem>): String {
        return Gson().toJson(list)
    }
}
