package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse
import com.app.activeparks.data.network.NetworkManager

class ClubsRepositoryImpl(
private val networkManager: NetworkManager
) : ClubsRepository {
    override suspend fun getClubList(): ClubListResponse? {
        return networkManager.getClubList()
    }

}