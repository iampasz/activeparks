package com.app.activeparks.data.repository.participants

import com.app.activeparks.data.model.user.UserParticipants

interface ParticipantsRepository {
     suspend fun getUserApplying(id: String): UserParticipants?
     suspend fun getClubUsers(id: String, userType:String): UserParticipants?

     suspend fun getHeadsClubUsers(id: String): UserParticipants?
     suspend fun getApplyingClubUsers(id: String): UserParticipants?
     suspend fun getMembersClubUsers(id: String): UserParticipants?

     //Events
     suspend fun getHeadsEventUsers(id: String): UserParticipants?
     suspend fun getApplyingEventUsers(id: String): UserParticipants?
     suspend fun getMembersEventUsers(id: String): UserParticipants?
}