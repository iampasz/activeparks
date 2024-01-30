package com.app.activeparks.data.useCase.trackState

import com.app.activeparks.data.model.track.AddTrackResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse

interface TrackStateUseCase {
    suspend fun insert(): TrackResponse?
    suspend fun saveTrack(id: String, trackState: TrackResponse)
    suspend fun getTrack(id: String): TrackResponse?
    suspend fun deleteTrack(id: String)
    suspend fun getTracks(name: String): ListTrackResponse?
}