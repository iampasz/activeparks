package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Entity(tableName = "active")
data class ActiveEntity(
    @PrimaryKey(autoGenerate = false)
    var idLocal: String,
    @ColumnInfo("notes") val notes: String,
    @ColumnInfo("time") val time: Long,
    @ColumnInfo("startsAt") val startsAt: Long,
    @ColumnInfo("finishesAt") val finishesAt: Long,
    @ColumnInfo("bpm") val bpm: Int,
    @ColumnInfo("maxBpm") val maxBpm: Int,
    @ColumnInfo("minBpm") val minBpm: Int,
    @ColumnInfo("speedMax") val speedMax: Int,
    @ColumnInfo("speed") val speed: Int,
    @ColumnInfo("temp") val temp: Int,
    @ColumnInfo("calories") val calories: Long,
    @ColumnInfo("weatherIcon") val weatherIcon: String,
    @ColumnInfo("temperature") val temperature: String,
    @ColumnInfo("hight") val height: Int,
    @ColumnInfo("feelingId") val feelingId: Int,
    @ColumnInfo("weather") val weather: String,
    @ColumnInfo("routePoints") val routePoints: List<List<Double>>,
    @ColumnInfo("coverImage") val coverImage: String,
    @ColumnInfo("photos") val photos: List<String>,
    @ColumnInfo("distance") val distance: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("activityType") val activityType: String,
    @ColumnInfo("complexityId") val complexityId: Int,
    @ColumnInfo("address") val address: String,
    @ColumnInfo("isActiveRoute") val isActiveRoute: String,
    @ColumnInfo("idActiveRoutes") val idActiveRoutes: String?,
    @ColumnInfo("routeActivePoints") val routeActivePoints: List<List<Double>>
)