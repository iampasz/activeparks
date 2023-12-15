package com.app.activeparks.data.repository.pauseActivity

import com.app.activeparks.data.db.dao.ActivityPauseDao
import com.app.activeparks.data.db.mapper.ActivityPauseMapper
import com.app.activeparks.ui.active.model.ActivityState

/**
 * Created by O.Dziuba on 15.12.2023.
 */
class PauseActivityRepositoryImpl(
private val dao: ActivityPauseDao
) : PauseActivityRepository {
    override suspend fun insert(active: ActivityState) {
        dao.saveActivityState(ActivityPauseMapper.toEntity(active))
    }

    override suspend fun getActive(): ActivityState? {
        return ActivityPauseMapper.toState(dao.getActivityState())
    }

    override suspend fun delete() {
        dao.deleteActivityState()
    }

}