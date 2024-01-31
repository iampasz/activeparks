package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub

interface ClubsRepository {
    suspend fun getClubList(): ClubListResponse?
    suspend fun getCombinatedClubList(): ClubsCombinedResponse?
    suspend fun getClubsDetails(id:String): ItemClub?
}