package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.clubs.UserInviteDeclaration
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.network.NetworkManager

class ClubsRepositoryImpl(
private val networkManager: NetworkManager
) : ClubsRepository {
    override suspend fun getClubList(): ClubListResponse? {
        return networkManager.getClubList()
    }

    override suspend fun getCombinatedClubList(): ClubsCombinedResponse? {
        return networkManager.getCombinatedClubList()
    }

    override suspend fun getClubsDetails(id:String): ItemClub? {
        return networkManager.getClubsDetails(id)
    }

    override suspend fun getApplyUser(id:String, request: UserInviteDeclaration): Boolean? {
        return networkManager.getApplyUser(id, request)
    }

    override suspend fun getRejectUser(id: String, request: UserInviteDeclaration): Boolean? {
        return networkManager.getRejectUser(id, request)
    }

    override suspend fun getClubNewsList(id: String): NewsListResponse? {
        return networkManager.getClubNewsList(id)
    }
}