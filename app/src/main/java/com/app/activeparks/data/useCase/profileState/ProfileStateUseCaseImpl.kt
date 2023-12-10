package com.app.activeparks.data.useCase.profileState

import com.app.activeparks.data.repository.profileState.ProfileStateRepository
import com.app.activeparks.ui.active.model.ProfileState

class ProfileStateUseCaseImpl(
    private val repository: ProfileStateRepository
) : ProfileStateUseCase {
    override suspend fun saveProfileState(profileState: ProfileState) {
        repository.saveProfileState(profileState)
    }

    override suspend fun getProfileState(): ProfileState? {
        return repository.getProfileState()
    }

    override suspend fun deleteProfileState(profileState: ProfileState) {
        repository.deleteProfileState(profileState)
    }
}