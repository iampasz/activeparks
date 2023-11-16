package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.app.activeparks.ui.active.model.TypeOfActivity

/**
 * Created by O.Dziuba on 16.11.2023.
 */

@Entity(tableName = "activity_state_table")
data class ActivityStateEntity(
    @PrimaryKey(autoGenerate = false)
    var keyId: Long = 0,
    @Embedded(prefix = "activity_type_")
    val activityType: TypeOfActivity,
    @Embedded(prefix = "activity_type_outside_")
    val activityTypeOutside: LevelOfActivity,
    @ColumnInfo(name = "pulse_on_pause")
    val pulseOnPause: Int,
    @ColumnInfo(name = "max_pulse")
    val maxPulse: Int,
    @Embedded(prefix = "ai_first_")
    val aiFirst: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_second_")
    val aiSecond: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_third_")
    val aiThird: ActivityInfoTrainingItem,
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