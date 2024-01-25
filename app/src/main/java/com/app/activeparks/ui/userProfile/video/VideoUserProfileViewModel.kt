package com.app.activeparks.ui.userProfile.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.uservideo.UserVideo
import com.app.activeparks.data.model.uservideo.UserVideoItem
import com.app.activeparks.data.model.uservideo.VideosResponse
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.data.useCase.video.VideoUseCase
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.ui.userProfile.video.model.VideoSelector
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by O.Dziuba on 12.01.2024.
 */
class VideoUserProfileViewModel(
    private val preferences: Preferences,
    private val videoUseCase: VideoUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    val videoCategory: MutableLiveData<List<VideoSelector>> = MutableLiveData()
    val videoLevel: MutableLiveData<List<VideoSelector>> = MutableLiveData()
    val userVideos: MutableLiveData<VideosResponse> = MutableLiveData()
    val closed: MutableLiveData<Boolean> = MutableLiveData(false)
    val update: MutableLiveData<Boolean> = MutableLiveData(false)

    var videoItem = UserVideoItem()

    fun getVideoValue() {
        getVideoCategory()
        getVideoLevel()
    }

    fun createUserVideo() {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.createUserVideo()
            }.onSuccess { item ->
                item?.let {
                    videoItem.apply {
                        createdAt = it.createdAt
                        id = it.id
                        updatedAt = it.updatedAt
                        statusId = it.statusId
                    }
                    updateUserVideo()
                }
            }
        }
    }

    fun getUserVideo(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.getUserVideo(id)
            }.onSuccess { item ->
                item?.let {
                    videoItem = it
                    update.value = true
                }
            }
        }
    }
    fun getUserVideos() {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.getUserVideos()
            }.onSuccess { item ->
                item?.let { userVideos.value = it }
            }
        }
    }

    fun updateUserVideo() {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.updateUserVideo(videoItem.id, videoItem)
            }.onSuccess {
                sendUserVideo()
            }
        }
    }

    private fun sendUserVideo() {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.sendUserVideo(videoItem.id)
            }.onSuccess {
                closed.value = true
            }
        }
    }

    fun deleteUserVideo() {
        viewModelScope.launch {
            kotlin.runCatching {
                videoUseCase.deleteUserVideo(videoItem.id)
            }.onSuccess {
                closed.value = true
            }
        }
    }

    private fun getVideoCategory() {
        val categories = mutableListOf<VideoSelector>()
        for (district in preferences.dictionarie.exerciseCategories) {
            categories.add(VideoSelector(district.id, district.title))
        }
        videoCategory.value = categories
    }

    private fun getVideoLevel() {
        val levels = mutableListOf<VideoSelector>()
        for (difficulty in preferences.dictionarie.exerciseDifficultyLevels) {
            levels.add(VideoSelector(difficulty.id, difficulty.title))
        }
        videoLevel.value = levels
    }

    fun updateImg(file: File) {
        viewModelScope.launch {
            kotlin.runCatching {
                uploadFileUseCase.updateFile("other_photo", file)
            }.onSuccess { response ->
                response?.url?.let { url ->
                    videoItem.mainPhoto = url
                }
            }
        }
    }
}