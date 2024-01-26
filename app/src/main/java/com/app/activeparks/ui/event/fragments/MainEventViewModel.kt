package com.app.activeparks.ui.event.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.gallery.PhotoGalleryResponse
import com.app.activeparks.data.model.sportevents.EventResponse
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import com.app.activeparks.data.useCase.photoGallery.PhotoGalleryUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import kotlinx.coroutines.launch
import java.io.File

class MainEventViewModel(
    private val eventStateUseCase: EventStateUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val photoGalleryUseCase: PhotoGalleryUseCase
) : ViewModel() {

    val eventList = MutableLiveData<EventResponse>()
    val imageLink = MutableLiveData<String>()
    val userPhotoUpdated = MutableLiveData<PhotoGalleryResponse>()
    val officialPhotoUpdated = MutableLiveData<PhotoGalleryResponse>()

    fun getAdminEvents() {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.getAdminEvents()

            }.onSuccess { response ->
                response?.let {
                    eventList.value = it
                }
            }
        }
    }

    fun setDataEvent(id: String, itemEvent: ItemEvent) {
        viewModelScope.launch {
            kotlin.runCatching {
                eventStateUseCase.setDataEvent(
                    id,
                    itemEvent
                )
            }.onSuccess { response ->
                response?.let {

                }
            }
        }
    }

    fun uploadFile(file: File, type: String, itemCurrentId: String?) {
        viewModelScope.launch {
            kotlin.runCatching {
                uploadFileUseCase.uploadFile(
                    type,
                    file,
                    itemCurrentId
                )
            }.onSuccess { response ->
                response?.let {
                    imageLink.value = it.url
                }
            }
        }
    }

    fun getPhotoGalleryOfficial(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                photoGalleryUseCase.getPhotoGalleryOfficial(id)
            }.onSuccess { response ->
                response?.let {
                }
            }
        }
    }

    fun getPhotoGalleryUser(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                photoGalleryUseCase.getPhotoGalleryUser(id)
            }.onSuccess { response ->
                response?.let {
                    userPhotoUpdated.value = it
                }
            }
        }
    }


}