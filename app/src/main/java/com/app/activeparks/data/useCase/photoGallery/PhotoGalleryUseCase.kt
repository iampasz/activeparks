package com.app.activeparks.data.useCase.photoGallery

import com.app.activeparks.data.model.gallery.PhotoGalleryResponse

interface PhotoGalleryUseCase {
    suspend fun getPhotoGalleryOfficial(id:String):PhotoGalleryResponse?
    suspend fun getPhotoGalleryUser(id:String): PhotoGalleryResponse?
}