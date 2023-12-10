package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.ProfileStateEntity
import com.app.activeparks.ui.active.model.ProfileState


/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ProfileStateMapper {

    companion object { 
        fun mapToEntity(profileState: ProfileState): ProfileStateEntity {
            return ProfileStateEntity(

                pulseOnPause = profileState.pulseOnPause,
                maxPulse = profileState.maxPulse,

                isAutoPause = profileState.isAutoPause,
                isAudioHelper = profileState.isAudioHelper,
                isLazyStart = profileState.isLazyStart,
                isAutoPulseZone = profileState.isAutoPulseZone,
                isAutoBlockScreen = profileState.isAutoBlockScreen
            )
        }

        fun mapToModel(profileStateEntity: ProfileStateEntity): ProfileState {
            return ProfileState(

                pulseOnPause = profileStateEntity.pulseOnPause,
                maxPulse = profileStateEntity.maxPulse,

                isAutoPause = profileStateEntity.isAutoPause,
                isAudioHelper = profileStateEntity.isAudioHelper,
                isLazyStart = profileStateEntity.isLazyStart,
                isAutoPulseZone = profileStateEntity.isAutoPulseZone,
                isAutoBlockScreen = profileStateEntity.isAutoBlockScreen
            )
        }
    }
}