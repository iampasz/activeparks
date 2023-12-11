package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profile_state_table")
data class ProfileStateEntity(
    @PrimaryKey(autoGenerate = false)
    var keyId: Long = 0,


    @ColumnInfo(name = "pulse_on_pause")
    val pulseOnPause: Int,
    @ColumnInfo(name = "max_pulse")
    val maxPulse: Int,


    @ColumnInfo(name = "is_auto_pause")
    val isAutoPause: Boolean,
    @ColumnInfo(name = "is_audio_helper")
    val isAudioHelper: Boolean,
    @ColumnInfo(name = "is_lazy_start")
    val isLazyStart: Boolean,
    @ColumnInfo(name = "is_auto_pulse_zone")
    val isAutoPulseZone: Boolean,
    @ColumnInfo(name = "is_auto_block_screen")
    val isAutoBlockScreen: Boolean
)