package com.app.activeparks.data.repository.uploadFile

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.events.ImageLinkResponse
import com.app.activeparks.data.network.NetworkManager
import java.io.File

/**
 * Created by O.Dziuba on 04.01.2024.
 */
class UploadFileRepositoryImpl(
    private val networkManager: NetworkManager
) : UploadFileRepository {

    override suspend fun updateFile(type: String, file: File): Default? {
        return networkManager.updateFile(type, file)
    }

    override suspend fun uploadFile(
        type: String,
        file: File,
        itemCurrentId: String?
    ): ImageLinkResponse? {
        return networkManager.uploadFile(
            type,
            file,
            itemCurrentId
        )
    }
}