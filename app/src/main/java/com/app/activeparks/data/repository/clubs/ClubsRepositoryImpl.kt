package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.model.clubs.ClubsCombinedResponse
import com.app.activeparks.data.model.clubs.ItemClub
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






}