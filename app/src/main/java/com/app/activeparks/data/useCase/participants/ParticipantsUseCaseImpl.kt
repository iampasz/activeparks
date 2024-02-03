package com.app.activeparks.data.useCase.participants

import com.app.activeparks.data.model.user.UserParticipants
import com.app.activeparks.data.repository.participants.ParticipantsRepository

class ParticipantsUseCaseImpl(
    val repository: ParticipantsRepository
): ParticipantsUseCase {
    override suspend fun getUserApplying(id: String): UserParticipants? {
       return repository.getUserApplying(id)
    }

    override suspend fun getClubUsers(id: String, userType: String): UserParticipants? {
        return repository.getClubUsers(id, userType)
    }

    override suspend fun getHeadsClubUsers(id: String): UserParticipants? {
        return repository.getHeadsClubUsers(id)
    }

    override suspend fun getApplyingClubUsers(id: String): UserParticipants? {
        return repository.getApplyingClubUsers(id)
    }

    override suspend fun getMembersClubUsers(id: String): UserParticipants? {
        return repository.getMembersClubUsers(id)
    }

    override suspend fun getHeadsEventUsers(id: String): UserParticipants? {
        return repository.getHeadsEventUsers(id)
    }

    override suspend fun getApplyingEventUsers(id: String): UserParticipants? {
        return repository.getApplyingEventUsers(id)
    }

    override suspend fun getMembersEventUsers(id: String): UserParticipants? {
        return repository.getMembersEventUsers(id)
    }
}