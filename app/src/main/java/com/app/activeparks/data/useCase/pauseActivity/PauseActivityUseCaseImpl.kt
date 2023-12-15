package com.app.activeparks.data.useCase.pauseActivity

import com.app.activeparks.data.repository.pauseActivity.PauseActivityRepository
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 15.12.2023.
 */
class PauseActivityUseCaseImpl(
    private val pauseActivityRepository: PauseActivityRepository
) : PauseActivityUseCase {
    override suspend fun insert(active: ActivityState) {
        return pauseActivityRepository.insert(active)
    }

    override suspend fun getActive(): ActivityState? {
        return pauseActivityRepository.getActive()
    }

    override suspend fun delete() {
        pauseActivityRepository.delete()
    }
}