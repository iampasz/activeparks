package com.app.activeparks.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 13.11.2023.
 */
class GeoPointConverters {
    @TypeConverter
    fun fromGeoPointsList(geoPoints: List<GeoPoint>): String {
        val gson = Gson()
        return gson.toJson(geoPoints)
    }

    @TypeConverter
    fun toGeoPointsList(geoPointsString: String): List<GeoPoint> {
        val listType = object : TypeToken<List<GeoPoint>>() {}.type
        val gson = Gson()
        return gson.fromJson(geoPointsString, listType)
    }
}