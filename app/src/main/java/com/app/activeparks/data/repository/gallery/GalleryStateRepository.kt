package com.app.activeparks.data.repository.gallery

import com.app.activeparks.data.model.gallery.PhotoGalleryResponse

interface GalleryStateRepository {
    suspend fun getPhotoGalleryOfficial(id:String): PhotoGalleryResponse?
    suspend fun getPhotoGalleryUser(id:String): PhotoGalleryResponse?
}
