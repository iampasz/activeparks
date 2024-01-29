package com.app.activeparks.data.repository.track

import com.app.activeparks.data.db.dao.ActiveDao
import com.app.activeparks.data.model.track.AddTrackResponse
import com.app.activeparks.data.model.track.ListTrackResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.network.NetworkManager

class TrackStateRepositoryImpl (
    private val networkManager: NetworkManager
) : TrackStateRepository {
    override suspend fun insert(): TrackResponse? {
        return networkManager.createTrack()
    }

    override suspend fun saveTrack(id: String, request: TrackResponse): TrackResponse?  {
        return networkManager.saveTrack(id, request)
    }

    override suspend fun getTrack(id: String): TrackResponse? {
        return networkManager.getTrack(id);
    }

    override suspend fun deleteTrack(id: String) {
        networkManager.removeTrack(id);
    }

    override suspend fun getTracks(name: String): ListTrackResponse? {
        return networkManager.getTracks(name)
    }

}