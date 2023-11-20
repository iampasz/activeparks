package com.app.activeparks.data.useCase.saveActivity

import com.app.activeparks.data.db.entity.ActiveEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface SaveActivityUseCase {

    suspend fun insert(active: ActiveEntity)
    suspend fun getActive(keyId: Long): ActiveEntity
    suspend fun getActives(): List<ActiveEntity>
}