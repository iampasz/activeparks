package com.app.activeparks.data.useCase.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub

interface ClubsUseCase {
    suspend fun getClubList(): ClubListResponse?
    suspend fun getCombinatedClubList(): ClubsCombinedResponse?
    suspend fun getClubsDetails(id:String): ItemClub?
}
