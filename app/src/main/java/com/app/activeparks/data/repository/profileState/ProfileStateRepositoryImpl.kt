package com.app.activeparks.data.repository.profileState

import com.app.activeparks.data.db.dao.ProfileStateDao
import com.app.activeparks.data.db.mapper.ProfileStateMapper
import com.app.activeparks.ui.active.model.ProfileState


class ProfileStateRepositoryImpl(
    private val profileStateDao: ProfileStateDao
) : ProfileStateRepository {

    override suspend fun saveProfileState(profileState: ProfileState) {
        val entity = ProfileStateMapper.mapToEntity(profileState)
        profileStateDao.saveProfileState(entity)
    }

    override suspend fun getProfileState(): ProfileState? {
        val entity = profileStateDao.getProfileState()
        return entity?.let { ProfileStateMapper.mapToModel(it) }
    }

    override suspend fun deleteProfileState(profileState: ProfileState) {
        val entity = ProfileStateMapper.mapToEntity(profileState)
        profileStateDao.deleteProfileState(entity)
    }
}