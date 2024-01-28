package com.app.activeparks.data.repository.gallery

import com.app.activeparks.data.model.gallery.PhotoGalleryResponse
import com.app.activeparks.data.network.NetworkManager

class GalleryStateRepositoryImpl(
    private val networkManager: NetworkManager
) : GalleryStateRepository {
    override suspend fun getPhotoGalleryOfficial(id: String): PhotoGalleryResponse? {
        return networkManager.getPhotoGalleryOfficial(id)
    }

    override suspend fun getPhotoGalleryUser(id: String): PhotoGalleryResponse? {
        return networkManager.getPhotoGalleryUser(id)
    }
}