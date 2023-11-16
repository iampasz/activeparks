package com.app.activeparks.data.useCase.activeState

import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
interface ActivityStateUseCase {
    suspend fun saveActivityState(activityState: ActivityState)
    suspend fun getActivityState(): ActivityState?
    suspend fun deleteActivityState(activityState: ActivityState)
}