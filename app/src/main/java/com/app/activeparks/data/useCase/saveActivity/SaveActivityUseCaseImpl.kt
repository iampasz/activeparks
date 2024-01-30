package com.app.activeparks.data.useCase.saveActivity

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository

/**
 * Created by O.Dziuba on 09.11.2023.
 */
class SaveActivityUseCaseImpl(
    val repository: SaveActivityRepository
) : SaveActivityUseCase {
    override suspend fun insert(active: ActiveEntity) {
        repository.insert(active)
    }

    override suspend fun updateActivity(id: String, request: AddActivityResponse): ResponseId? {
        return repository.updateActivity(id, request)
    }

    override suspend fun getActive(keyId: Long): ActiveEntity {
        return repository.getActive(keyId)
    }

    override suspend fun getActives(): List<ActiveEntity> {
        return repository.getActives()
    }

    override suspend fun getWorkoutsActivity(): ActivityResponse? {
        return repository.getWorkoutsActivity()
    }

    override suspend fun getWorkoutActivity(id: String): ActivityItemResponse? {
        return repository.getWorkoutActivity(id)
    }

    override suspend fun getWorkoutsActivity(
        startsFrom: String,
        startsTo: String
    ): ActivityResponse? {
        return repository.getWorkoutsActivity(startsFrom, startsTo)
    }
}