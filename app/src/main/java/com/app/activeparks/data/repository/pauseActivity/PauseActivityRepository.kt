package com.app.activeparks.data.repository.pauseActivity

import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface PauseActivityRepository {
    suspend fun insert(active: ActivityState)
    suspend fun getActive(): ActivityState?
    suspend fun delete()
}