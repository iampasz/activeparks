package com.app.activeparks.data.useCase.saveActivity

import com.app.activeparks.data.db.entity.Active
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository
import com.app.activeparks.ui.active.model.CurrentActivity

/**
 * Created by O.Dziuba on 09.11.2023.
 */
class SaveActivityUseCaseImpl(
    val repository: SaveActivityRepository
) : SaveActivityUseCase {
    override suspend fun insert(active: Active) {
        repository.insert(active)
    }

    override suspend fun getActive(keyId: Long): Active {
        return repository.getActive(keyId)
    }

    override suspend fun getActives(): List<Active> {
        return repository.getActives()
    }
}