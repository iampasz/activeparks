package com.app.activeparks.data.useCase.clubs

import com.app.activeparks.data.repository.clubs.ClubsRepository


class ClubsUseCaseImpl (
    private val repository: ClubsRepository
) : ClubsUseCase {
    override suspend fun getClubList() {
        repository.getClubList()
    }

}