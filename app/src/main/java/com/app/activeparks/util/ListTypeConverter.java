package com.app.activeparks.util;


import androidx.room.TypeConverter;

import com.app.activeparks.data.model.activity.GeoPointEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListTypeConverter  {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<GeoPointEntity> fromJson(String json) {
        Type type = new TypeToken<List<GeoPointEntity>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String toJson(List<GeoPointEntity> geoPointEntities) {
        Type type = new TypeToken<List<GeoPointEntity>>() {}.getType();
        return gson.toJson(geoPointEntities, type);
    }
}
