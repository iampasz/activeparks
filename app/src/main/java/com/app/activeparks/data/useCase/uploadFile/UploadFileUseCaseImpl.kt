package com.app.activeparks.data.useCase.uploadFile

import com.app.activeparks.data.model.Default
import com.app.activeparks.data.repository.uploadFile.UploadFileRepository
import java.io.File

/**
 * Created by O.Dziuba on 04.01.2024.
 */
class UploadFileUseCaseImpl(
    private val uploadFileRepository: UploadFileRepository
) : UploadFileUseCase {
    override suspend fun updateFile(type: String, file: File): Default? {
        return uploadFileRepository.updateFile(type, file)
    }
}