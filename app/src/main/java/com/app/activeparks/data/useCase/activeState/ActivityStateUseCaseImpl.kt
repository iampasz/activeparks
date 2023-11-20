package com.app.activeparks.data.useCase.activeState

import com.app.activeparks.data.repository.ctiveState.ActivityStateRepository
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityStateUseCaseImpl(
    private val repository: ActivityStateRepository
) : ActivityStateUseCase {
    override suspend fun saveActivityState(activityState: ActivityState) {
        repository.saveActivityState(activityState)
    }

    override suspend fun getActivityState(): ActivityState? {
        return repository.getActivityState()
    }

    override suspend fun deleteActivityState(activityState: ActivityState) {
        repository.deleteActivityState(activityState)
    }
}