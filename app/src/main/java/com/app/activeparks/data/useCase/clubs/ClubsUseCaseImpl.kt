package com.app.activeparks.data.useCase.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
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

}