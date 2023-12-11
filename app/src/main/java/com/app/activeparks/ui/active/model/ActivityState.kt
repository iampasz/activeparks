package com.app.activeparks.ui.active.model

import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityState(
    var activityType: TypeOfActivity = TypeOfActivity.getTypeOfActivity().first(),
    var activityTypeOutside: LevelOfActivity = LevelOfActivity.getLevelOfActivity().first(),
    val activeRoad: MutableList<GeoPoint> = mutableListOf(),
    var startPoint: String = "",
    var weather: String = "",
    var weatherIcon: Int = -1,
    var currentPulse: Int = 80,
    var pulseOnPause: Int = 75,
    var maxPulse: Int = 150,
    var pulseZone: PulseZone = PulseZone.getPulseZone().first(),
    var aiFirst: ActivityInfoTrainingItem =  ActivityInfoTrainingItem.getActivityInfoItem()[0],
    var aiSecond: ActivityInfoTrainingItem =  ActivityInfoTrainingItem.getActivityInfoItem()[1],
    var aiThird: ActivityInfoTrainingItem =  ActivityInfoTrainingItem.getActivityInfoItem()[2],
    var onSelectedTypeFromSetting: Boolean = false,
    var isPulseGadgetConnected: Boolean = true,
    var isAutoPause: Boolean = false,
    var isAudioHelper: Boolean = false,
    var isLazyStart: Boolean = false,
    var isTrainingStart: Boolean = false,
    var isPause: Boolean = false,
    var isAutoPulseZone: Boolean = true,
    var isAutoBlockScreen: Boolean = false,
)
