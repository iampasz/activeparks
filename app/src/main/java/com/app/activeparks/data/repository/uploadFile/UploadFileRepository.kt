package com.app.activeparks.data.repository.uploadFile

import com.app.activeparks.data.model.Default
import java.io.File

/**
 * Created by O.Dziuba on 04.01.2024.
 */
interface UploadFileRepository {
    suspend fun updateFile(
        type: String,
        file: File
    ): Default?
}