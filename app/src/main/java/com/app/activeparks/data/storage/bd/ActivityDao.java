package com.app.activeparks.data.storage.bd;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.activeparks.data.model.activity.GeoPointList;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ActivityDao {

    @Query("SELECT * FROM geoPointList")
    Single<List<GeoPointList>> getAll();


    @Insert
    Completable insert(GeoPointList geoPointList);

    @Delete
    void delete(GeoPointList geoPointList);
}
