package com.app.activeparks.ui.active.model

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityState(
    var activityType: TypeOfActivity = TypeOfActivity.getTypeOfActivity().first(),
    var activityTypeOutside: LevelOfActivity = LevelOfActivity.getLevelOfActivity().first(),
    var pulseZone: PulseZone = PulseZone.getPulseZone().first(),
    var aiFirst: ActivityInfoItem =  ActivityInfoItem.getActivityInfoItem()[0],
    var aiSecond: ActivityInfoItem =  ActivityInfoItem.getActivityInfoItem()[1],
    var aiThird: ActivityInfoItem =  ActivityInfoItem.getActivityInfoItem()[2],
    var onSelectedTypeFromSetting: Boolean = false,
    var isPulseGadgetConnected: Boolean = true,
    var isAutoPause: Boolean = false,
    var isAudioHelper: Boolean = false,
    var isLazyStart: Boolean = false,
    var isTrainingStart: Boolean = false,
    var isAutoPulseZone: Boolean = false,
    var isAutoBlockScreen: Boolean = false,
)
