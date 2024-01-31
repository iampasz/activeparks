package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.ActivityPauseEntity
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityPauseMapper {

    companion object {
        fun toEntity(
            activityState: ActivityState
        ): ActivityPauseEntity {
            return ActivityPauseEntity(
                activityType = activityState.activityType,
                activityTypeOutside = activityState.activityTypeOutside,
                activeRoad = activityState.activityRoad,
                startPoint = activityState.startPoint,
                weather = activityState.weather,
                weatherIcon = activityState.weatherIcon,
                currentPulse = activityState.currentPulse,
                pulseOnPause = activityState.pulseOnPause,
                maxPulse = activityState.maxPulse,
                pulseZone = activityState.pulseZone,
                aiFirst = activityState.aiFirst,
                aiSecond = activityState.aiSecond,
                aiThird = activityState.aiThird,
                aiFirstOutside = activityState.aiFirstOutside,
                aiSecondOutside = activityState.aiSecondOutside,
                aiThirdOutside = activityState.aiThirdOutside,
                onSelectedTypeFromSetting = activityState.onSelectedTypeFromSetting,
                isPulseGadgetConnected = activityState.isPulseGadgetConnected,
                isAutoPause = activityState.isAutoPause,
                isAudioHelper = activityState.isAudioHelper,
                isLazyStart = activityState.isLazyStart,
                isTrainingStart = activityState.isTrainingStart,
                isPause = activityState.isPause,
                isAutoPulseZone = activityState.isAutoPulseZone,
                isAutoBlockScreen = activityState.isAutoBlockScreen,
                weight = activityState.weight,
                stepCount = activityState.stepCount,
                activityDuration = activityState.activityDuration,
                age = activityState.age,
                activityTime = activityState.activityTime,
                activityInfoItems = activityState.activityInfoItems,
                activityPulseItems = activityState.activityPulseItems
            )
        }

        fun toState(activityPauseEntity: ActivityPauseEntity?): ActivityState? {
            return activityPauseEntity?.let {
                ActivityState(
                    activityType = activityPauseEntity.activityType,
                    activityTypeOutside = activityPauseEntity.activityTypeOutside,
                    activityRoad = activityPauseEntity.activeRoad,
                    startPoint = activityPauseEntity.startPoint,
                    weather = activityPauseEntity.weather,
                    weatherIcon = activityPauseEntity.weatherIcon,
                    currentPulse = activityPauseEntity.currentPulse,
                    pulseOnPause = activityPauseEntity.pulseOnPause,
                    maxPulse = activityPauseEntity.maxPulse,
                    pulseZone = activityPauseEntity.pulseZone,
                    aiFirst = activityPauseEntity.aiFirst,
                    aiSecond = activityPauseEntity.aiSecond,
                    aiThird = activityPauseEntity.aiThird,
                    aiFirstOutside = activityPauseEntity.aiFirstOutside,
                    aiSecondOutside = activityPauseEntity.aiSecondOutside,
                    aiThirdOutside = activityPauseEntity.aiThirdOutside,
                    onSelectedTypeFromSetting = activityPauseEntity.onSelectedTypeFromSetting,
                    isPulseGadgetConnected = activityPauseEntity.isPulseGadgetConnected,
                    isAutoPause = activityPauseEntity.isAutoPause,
                    isAudioHelper = activityPauseEntity.isAudioHelper,
                    isLazyStart = activityPauseEntity.isLazyStart,
                    isTrainingStart = activityPauseEntity.isTrainingStart,
                    isPause = activityPauseEntity.isPause,
                    isAutoPulseZone = activityPauseEntity.isAutoPulseZone,
                    isAutoBlockScreen = activityPauseEntity.isAutoBlockScreen,
                    weight = activityPauseEntity.weight,
                    stepCount = activityPauseEntity.stepCount,
                    activityDuration = activityPauseEntity.activityDuration,
                    age = activityPauseEntity.age,
                    activityTime = activityPauseEntity.activityTime,
                    activityInfoItems = activityPauseEntity.activityInfoItems,
                    activityPulseItems = activityPauseEntity.activityPulseItems
                )
            }
        }
    }
}