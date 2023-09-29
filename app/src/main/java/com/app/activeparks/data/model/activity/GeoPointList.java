package com.app.activeparks.data.model.activity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.app.activeparks.util.ListTypeConverter;

import org.osmdroid.util.GeoPoint;

import java.util.List;

@Entity
public class GeoPointList {
    @PrimaryKey(autoGenerate = true)
    public int keyId;

    @TypeConverters({ListTypeConverter.class})
    List<GeoPointEntity> list;

    public GeoPointList(List<GeoPointEntity> list) {
        this.list = list;
    }

    public List<GeoPointEntity> getList() {
        return list;
    }
}
