package com.app.activeparks.data.useCase.trackState

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.ActivityResponse
import com.app.activeparks.data.model.track.AddTrackResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository
import com.app.activeparks.data.repository.track.TrackStateRepository

class TrackStateUseCaseImpl (
    val repository: TrackStateRepository
    ) : TrackStateUseCase {
        override suspend fun saveTrack(id: String, trackState: TrackResponse) {
            repository.saveTrack(id, trackState)
        }

        override suspend fun getTrack(id: String): TrackResponse? {
            return repository.getTrack(id)
        }

        override suspend fun deleteTrack(id: String) {
            repository.deleteTrack(id)
        }

        override suspend fun getTracks(name: String): ListTrackResponse? {
            return repository.getTracks(name)
        }

        override suspend fun insert(): TrackResponse? {
            return repository.insert()
        }

}