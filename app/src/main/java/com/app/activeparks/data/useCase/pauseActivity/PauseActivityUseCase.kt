package com.app.activeparks.data.useCase.pauseActivity

import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 15.12.2023.
 */
interface PauseActivityUseCase {
    suspend fun insert(active: ActivityState)
    suspend fun getActive(): ActivityState?
    suspend fun delete()
}