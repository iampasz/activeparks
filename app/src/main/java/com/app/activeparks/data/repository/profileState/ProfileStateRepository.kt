package com.app.activeparks.data.repository.profileState

import com.app.activeparks.ui.active.model.ProfileState

interface ProfileStateRepository {
    suspend fun saveProfileState(profileState: ProfileState)
    suspend fun getProfileState(): ProfileState?
    suspend fun deleteProfileState(profileState: ProfileState)
}