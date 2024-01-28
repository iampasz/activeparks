package com.app.activeparks.data.useCase.photoGallery

import com.app.activeparks.data.model.gallery.PhotoGalleryResponse
import com.app.activeparks.data.repository.gallery.GalleryStateRepository

class PhotoGalleryUseCaseImpl(
    private val repository: GalleryStateRepository
) : PhotoGalleryUseCase {
    override suspend fun getPhotoGalleryOfficial(id: String): PhotoGalleryResponse? {
        return repository.getPhotoGalleryOfficial(id)
    }

    override suspend fun getPhotoGalleryUser(id: String): PhotoGalleryResponse? {
        return repository.getPhotoGalleryUser(id)
    }
}