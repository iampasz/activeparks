package com.app.activeparks.data.repository.participants

import com.app.activeparks.data.model.user.UserParticipants
import com.app.activeparks.data.network.NetworkManager

class ParticipantsRepositoryImpl(
    private val networkManager: NetworkManager
) : ParticipantsRepository {
    override suspend fun getUserApplying(id: String): UserParticipants? {
       return networkManager.getUserApplying(id)
    }

    override suspend fun getClubUsers(id: String, userType: String): UserParticipants? {
        return networkManager.getClubUsers(id, userType)
    }

    override suspend fun getHeadsClubUsers(id: String): UserParticipants? {
        return networkManager.getHeadsClubUsers(id)
    }

    override suspend fun getApplyingClubUsers(id: String): UserParticipants? {
        return networkManager.getApplyingClubUsers(id)
    }

    override suspend fun getMembersClubUsers(id: String): UserParticipants? {
        return networkManager.getMembersClubUsers(id)
    }

    override suspend fun getHeadsEventUsers(id: String): UserParticipants? {
        return networkManager.getHeadsEventUsers(id)
    }

    override suspend fun getApplyingEventUsers(id: String): UserParticipants? {
        return networkManager.getApplyingEventUsers(id)
    }

    override suspend fun getMembersEventUsers(id: String): UserParticipants? {
        return networkManager.getMembersEventUsers(id)
    }
}