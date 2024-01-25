package com.app.activeparks.data.repository.video

import com.app.activeparks.data.model.uservideo.UserVideo
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import okhttp3.ResponseBody

/**
 * Created by O.Dziuba on 12.01.2024.
 */
interface VideoRepository {
    suspend fun createUserVideo(): UserVideoItem?
    suspend fun getUserVideo(id: String): UserVideoItem?
    suspend fun getUserVideos(): VideosResponse?
    suspend fun updateUserVideo(id: String, userVideoItem: UserVideoItem)
    suspend fun sendUserVideo(id: String): ResponseBody?
    suspend fun deleteUserVideo(id: String): ResponseBody?
}