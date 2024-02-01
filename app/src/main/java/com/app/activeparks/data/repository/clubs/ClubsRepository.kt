package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.clubs.UserInviteDeclaration
import com.app.activeparks.data.model.news.NewsListResponse

interface ClubsRepository {
    suspend fun getClubList(): ClubListResponse?
    suspend fun getCombinatedClubList(): ClubsCombinedResponse?
    suspend fun getClubsDetails(id:String): ItemClub?
    suspend fun getApplyUser(id:String, request: UserInviteDeclaration): Boolean?
    suspend fun getRejectUser(id:String, request: UserInviteDeclaration): Boolean?
    suspend fun getClubNewsList(id:String): NewsListResponse?
}