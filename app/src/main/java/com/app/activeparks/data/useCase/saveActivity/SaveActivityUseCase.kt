package com.app.activeparks.data.useCase.saveActivity

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.activity.AddActivityResponse
import com.app.activeparks.data.model.registration.ResponseId

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface SaveActivityUseCase {

    suspend fun insert(active: ActiveEntity)
    suspend fun updateActivity(id: String,request: AddActivityResponse): ResponseId?
    suspend fun getActive(keyId: Long): ActiveEntity
    suspend fun getActives(): List<ActiveEntity>
    suspend fun getWorkoutsActivity(): ActivityResponse?
    suspend fun getWorkoutActivity(id: String): ActivityItemResponse?
    suspend fun getWorkoutsActivity(
        startsFrom: String,
        startsTo: String): ActivityResponse?
}