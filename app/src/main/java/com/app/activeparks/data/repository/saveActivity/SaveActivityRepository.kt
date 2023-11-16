package com.app.activeparks.data.repository.saveActivity

import com.app.activeparks.data.db.entity.ActiveEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface SaveActivityRepository {
    suspend fun insert(active: ActiveEntity)
    suspend fun getActive(keyId: Long): ActiveEntity
    suspend fun getActives(): List<ActiveEntity>
}