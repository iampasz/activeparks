package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.ActivityStateEntity
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityStateMapper {

    companion object {
        fun mapToEntity(activityState: ActivityState): ActivityStateEntity {
            return ActivityStateEntity(
                activityType = activityState.activityType,
                activityTypeOutside = activityState.activityTypeOutside,
                pulseOnPause = activityState.pulseOnPause,
                maxPulse = activityState.maxPulse,
                aiFirst = activityState.aiFirst,
                aiSecond = activityState.aiSecond,
                aiThird = activityState.aiThird,
                isAutoPause = activityState.isAutoPause,
                isAudioHelper = activityState.isAudioHelper,
                isLazyStart = activityState.isLazyStart,
                isAutoPulseZone = activityState.isAutoPulseZone,
                isAutoBlockScreen = activityState.isAutoBlockScreen
            )
        }

        fun mapToModel(activityStateEntity: ActivityStateEntity): ActivityState {
            return ActivityState(
                activityType = activityStateEntity.activityType,
                activityTypeOutside = activityStateEntity.activityTypeOutside,
                pulseOnPause = activityStateEntity.pulseOnPause,
                maxPulse = activityStateEntity.maxPulse,
                aiFirst = activityStateEntity.aiFirst,
                aiSecond = activityStateEntity.aiSecond,
                aiThird = activityStateEntity.aiThird,
                isAutoPause = activityStateEntity.isAutoPause,
                isAudioHelper = activityStateEntity.isAudioHelper,
                isLazyStart = activityStateEntity.isLazyStart,
                isAutoPulseZone = activityStateEntity.isAutoPulseZone,
                isAutoBlockScreen = activityStateEntity.isAutoBlockScreen
            )
        }
    }
}