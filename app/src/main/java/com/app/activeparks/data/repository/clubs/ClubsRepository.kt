package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse

interface ClubsRepository {
    suspend fun getClubList(): ClubListResponse?
    suspend fun getCombinatedClubList(id:String): ClubsCombinedResponse?
}