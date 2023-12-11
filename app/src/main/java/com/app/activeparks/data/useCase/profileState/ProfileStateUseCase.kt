package com.app.activeparks.data.useCase.profileState

import com.app.activeparks.ui.active.model.ProfileState

interface ProfileStateUseCase {
    suspend fun saveProfileState(profileState: ProfileState)
    suspend fun getProfileState(): ProfileState?
    suspend fun deleteProfileState(profileState: ProfileState)
}