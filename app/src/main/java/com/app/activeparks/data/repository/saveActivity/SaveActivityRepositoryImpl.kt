package com.app.activeparks.data.repository.saveActivity

import com.app.activeparks.data.db.dao.activity.ActiveDao
import com.app.activeparks.data.db.entity.Active
import com.app.activeparks.ui.active.model.CurrentActivity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
class SaveActivityRepositoryImpl(
    private val dao: ActiveDao
) : SaveActivityRepository {
    override suspend fun insert(active: Active) {
        dao.insert(active)
    }

    override suspend fun getActive(keyId: Long): Active {
        return dao.getActive(keyId)
    }

    override suspend fun getActives(): List<Active> {
        return dao.getActives()
    }
}