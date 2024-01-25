package com.app.activeparks.data.repository.video

import com.app.activeparks.data.model.uservideo.UserVideo
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import com.app.activeparks.data.network.NetworkManager
import okhttp3.ResponseBody

/**
 * Created by O.Dziuba on 12.01.2024.
 */
class VideoRepositoryImpl(
    private val networkManager: NetworkManager
) : VideoRepository {
    override suspend fun createUserVideo(): UserVideoItem? {
        return networkManager.createUserVideo()
    }

    override suspend fun getUserVideo(id: String): UserVideoItem? {
        return networkManager.getUserVideo(id)
    }
    override suspend fun getUserVideos(): VideosResponse? {
        return networkManager.getUserVideos()
    }

    override suspend fun updateUserVideo(id: String, userVideoItem: UserVideoItem) {
        return networkManager.updateUserVideo(id, userVideoItem)
    }

    override suspend fun sendUserVideo(id: String): ResponseBody? {
        return networkManager.sendUserVideo(id)
    }

    override suspend fun deleteUserVideo(id: String): ResponseBody? {
        return networkManager.deleteUserVideo(id)
    }
}