package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityTime
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.app.activeparks.ui.active.model.PulseZone
import com.app.activeparks.ui.active.model.TypeOfActivity
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 16.11.2023.
 */

@Entity(tableName = "activity_pause_table")
data class ActivityPauseEntity(
    @PrimaryKey(autoGenerate = true)
    var keyId: Long = 0,
    @Embedded(prefix = "activity_type_")
    val activityType: TypeOfActivity,
    @Embedded(prefix = "activity_type_outside_")
    val activityTypeOutside: LevelOfActivity,
    @ColumnInfo(name = "active_road_")
    val activeRoad: MutableList<GeoPoint>,
    @ColumnInfo(name = "start_point")
    var startPoint: String,
    @ColumnInfo(name = "weather")
    var weather: String,
    @ColumnInfo(name = "weather_icon")
    var weatherIcon: Int,
    @ColumnInfo(name = "current_pulse")
    var currentPulse: Int,
    @ColumnInfo(name = "pulse_on_pause")
    var pulseOnPause: Int,
    @ColumnInfo(name = "max_pulse")
    var maxPulse: Int,
    @Embedded(prefix = "pulse_zone_")
    var pulseZone: PulseZone,
    @Embedded(prefix = "ai_first_")
    var aiFirst: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_second_")
    var aiSecond: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_third_")
    var aiThird: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_first_outside_")
    var aiFirstOutside: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_second_outside_")
    var aiSecondOutside: ActivityInfoTrainingItem,
    @Embedded(prefix = "ai_third_outside_")
    var aiThirdOutside: ActivityInfoTrainingItem,
    @ColumnInfo(name = "on_selected_type_from_setting")
    var onSelectedTypeFromSetting: Boolean,
    @ColumnInfo(name = "is_pulse_gadget_connected")
    var isPulseGadgetConnected: Boolean,
    @ColumnInfo(name = "is_auto_pause")
    var isAutoPause: Boolean,
    @ColumnInfo(name = "is_audio_helper")
    var isAudioHelper: Boolean,
    @ColumnInfo(name = "is_lazy_start")
    var isLazyStart: Boolean,
    @ColumnInfo(name = "is_training_start")
    var isTrainingStart: Boolean,
    @ColumnInfo(name = "is_pause")
    var isPause: Boolean,
    @ColumnInfo(name = "is_auto_pulse_zone")
    var isAutoPulseZone: Boolean,
    @ColumnInfo(name = "is_auto_block_screen")
    var isAutoBlockScreen: Boolean,
    @ColumnInfo(name = "weight")
    var weight: Int,
    @ColumnInfo(name = "step_count")
    var stepCount: Int,
    @ColumnInfo(name = "activity_duration")
    var activityDuration: Int,
    @ColumnInfo(name = "age")
    var age: Int,
    @Embedded(prefix = "activity_time_")
    var activityTime: ActivityTime,
    @ColumnInfo(name = "activity_info_items")
    var activityInfoItems: List<ActivityInfoTrainingItem>,
    @ColumnInfo(name = "activity_pulse_items")
    var activityPulseItems: List<InfoItem>
)