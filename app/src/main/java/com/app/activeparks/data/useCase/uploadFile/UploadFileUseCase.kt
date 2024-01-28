package com.app.activeparks.data.useCase.uploadFile

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.events.ImageLinkResponse
import java.io.File

/**
 * Created by O.Dziuba on 04.01.2024.
 */
interface UploadFileUseCase {
    suspend fun updateFile(
        type: String,
        file: File
    ): Default?

    suspend fun uploadFile(
        type: String,
        file: File,
        itemCurrentId:String?
    ): ImageLinkResponse?
}