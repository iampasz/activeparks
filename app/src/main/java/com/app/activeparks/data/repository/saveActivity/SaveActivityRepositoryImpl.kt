package com.app.activeparks.data.repository.saveActivity

import com.app.activeparks.data.db.dao.ActiveDao
import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.registration.ResponseId
import com.app.activeparks.data.network.NetworkManager

/**
 * Created by O.Dziuba on 09.11.2023.
 */
class SaveActivityRepositoryImpl(
    private val networkManager: NetworkManager,
    private val dao: ActiveDao
) : SaveActivityRepository {
    override suspend fun insert(active: ActiveEntity) {
        networkManager.createActivity(AddActivityResponse.map(active))
        dao.insert(active)
    }

    override suspend fun updateActivity(id: String, request: AddActivityResponse): ResponseId? {
        return networkManager.updateActivity(id, request)
    }

    override suspend fun getActive(keyId: Long): ActiveEntity {
        return dao.getActive(keyId)
    }

    override suspend fun getActives(): List<ActiveEntity> {
        return dao.getActives()
    }

    override suspend fun getWorkoutsActivity(): ActivityResponse? {
        return networkManager.getWorkoutsActivity()
    }

    override suspend fun getWorkoutActivity(id: String): ActivityItemResponse? {
        return networkManager.getWorkoutActivity(id)
    }

    override suspend fun getWorkoutsActivity(
        startsFrom: String,
        startsTo: String
    ): ActivityResponse? {
        return networkManager.getWorkoutsActivity(startsFrom, startsTo)
    }
}