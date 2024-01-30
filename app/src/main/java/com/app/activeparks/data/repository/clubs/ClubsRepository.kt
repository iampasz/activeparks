package com.app.activeparks.data.repository.clubs

import com.app.activeparks.data.model.clubs.ClubListResponse

interface ClubsRepository {
    suspend fun getClubList(): ClubListResponse?
}