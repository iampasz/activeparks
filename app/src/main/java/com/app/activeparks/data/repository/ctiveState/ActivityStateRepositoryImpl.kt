package com.app.activeparks.data.repository.ctiveState

import com.app.activeparks.data.db.dao.ActivityStateDao
import com.app.activeparks.data.db.mapper.ActivityStateMapper
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityStateRepositoryImpl(
    private val activityStateDao: ActivityStateDao
) : ActivityStateRepository {

    override suspend fun saveActivityState(activityState: ActivityState) {
        val entity = ActivityStateMapper.mapToEntity(activityState)
        activityStateDao.saveActivityState(entity)
    }

    override suspend fun getActivityState(): ActivityState? {
        val entity = activityStateDao.getActivityState()
        return entity?.let { ActivityStateMapper.mapToModel(it) }
    }

    override suspend fun deleteActivityState(activityState: ActivityState) {
        val entity = ActivityStateMapper.mapToEntity(activityState)
        activityStateDao.deleteActivityState(entity)
    }
}