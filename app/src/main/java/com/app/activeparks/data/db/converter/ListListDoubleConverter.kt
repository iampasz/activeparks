package com.app.activeparks.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by O.Dziuba on 04.12.2023.
 */
class ListListDoubleConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<List<Double>> {
        val listType = object : TypeToken<List<List<Double>>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<List<Double>>): String {
        return gson.toJson(value)
    }
}