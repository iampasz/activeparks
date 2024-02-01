package com.app.activeparks.data.useCase.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.clubs.UserInviteDeclaration
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.repository.clubs.ClubsRepository


class ClubsUseCaseImpl (
    private val repository: ClubsRepository
) : ClubsUseCase {
    override suspend fun getClubList(): ClubListResponse? {
         return repository.getClubList()
    }

    override suspend fun getCombinatedClubList(): ClubsCombinedResponse? {
        return repository.getCombinatedClubList()
    }

    override suspend fun getClubsDetails(id:String): ItemClub? {
        return repository.getClubsDetails(id)
    }

    override suspend fun getApplyUser(id: String, request: UserInviteDeclaration): Boolean? {
        return repository.getApplyUser(id, request)
    }

    override suspend fun getRejectUser(id: String, request: UserInviteDeclaration): Boolean? {
        return repository.getRejectUser(id, request)
    }

    override suspend fun getClubNewsList(clubId: String): NewsListResponse? {
        return repository.getClubNewsList(clubId)
    }
}