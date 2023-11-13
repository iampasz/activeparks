package com.app.activeparks.data.useCase.saveActivity

import com.app.activeparks.data.db.entity.Active

/**
 * Created by O.Dziuba on 09.11.2023.
 */
interface SaveActivityUseCase {

    suspend fun insert(active: Active)
    suspend fun getActive(keyId: Long): Active
    suspend fun getActives(): List<Active>
}