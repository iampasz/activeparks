package com.app.activeparks.data.repository.ctiveState

import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
interface ActivityStateRepository {
    suspend fun saveActivityState(activityState: ActivityState)
    suspend fun getActivityState(): ActivityState?
    suspend fun deleteActivityState(activityState: ActivityState)
}