package com.app.activeparks.data.useCase.video

import com.app.activeparks.data.model.uservideo.UserVideo
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import com.app.activeparks.data.repository.video.VideoRepository
import okhttp3.ResponseBody

/**
 * Created by O.Dziuba on 12.01.2024.
 */
class VideoUseCaseImpl(
    private val videoRepository: VideoRepository
) : VideoUseCase {
    override suspend fun createUserVideo(): UserVideoItem? {
        return videoRepository.createUserVideo()
    }

    override suspend fun getUserVideo(id: String): UserVideoItem? {
        return videoRepository.getUserVideo(id)
    }

    override suspend fun getUserVideos(): VideosResponse? {
        return videoRepository.getUserVideos()
    }

    override suspend fun updateUserVideo(id: String, userVideoItem: UserVideoItem) {
        return videoRepository.updateUserVideo(id, userVideoItem)
    }

    override suspend fun sendUserVideo(id: String): ResponseBody? {
        return videoRepository.sendUserVideo(id)
    }

    override suspend fun deleteUserVideo(id: String): ResponseBody? {
        return videoRepository.deleteUserVideo(id)
    }
}