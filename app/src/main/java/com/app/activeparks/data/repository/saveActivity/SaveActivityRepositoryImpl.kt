package com.app.activeparks.data.repository.saveActivity

import com.app.activeparks.data.db.dao.ActiveDao
import com.app.activeparks.data.db.entity.ActiveEntity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
class SaveActivityRepositoryImpl(
    private val dao: ActiveDao
) : SaveActivityRepository {
    override suspend fun insert(active: ActiveEntity) {
        dao.insert(active)
    }

    override suspend fun getActive(keyId: Long): ActiveEntity {
        return dao.getActive(keyId)
    }

    override suspend fun getActives(): List<ActiveEntity> {
        return dao.getActives()
    }
}