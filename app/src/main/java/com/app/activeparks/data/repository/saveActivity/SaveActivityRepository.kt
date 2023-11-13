package com.app.activeparks.data.repository.saveActivity

import com.app.activeparks.data.db.entity.Active

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface SaveActivityRepository {
    suspend fun insert(active: Active)
    suspend fun getActive(keyId: Long): Active
    suspend fun getActives(): List<Active>
}