package com.app.activeparks.data.useCase.uploadFile

import com.app.activeparks.data.model.Default
import java.io.File

/**
 * Created by O.Dziuba on 04.01.2024.
 */
interface UploadFileUseCase {
    suspend fun updateFile(
        type: String,
        file: File
    ): Default?
}