package com.app.activeparks.data.repository.track

import com.app.activeparks.data.model.track.AddTrackResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse

interface TrackStateRepository {

    suspend fun insert() : TrackResponse?
    suspend fun saveTrack(id: String, request: TrackResponse) : TrackResponse?
    suspend fun getTrack(id: String): TrackResponse?
    suspend fun deleteTrack(id: String)
    suspend fun getTracks(name: String): ListTrackResponse?
}