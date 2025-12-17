package com.example.android.playground.imageupload.domain.usecase

import com.example.android.playground.imageupload.domain.model.ImageUploadResult
import com.example.android.playground.imageupload.domain.repository.ImageUploadRepository
import com.example.android.playground.imageupload.util.ImageUploadConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadMultipleImagesUseCase @Inject constructor(
    private val repository: ImageUploadRepository
) {
    operator fun invoke(count: Int): Flow<ImageUploadResult> = flow {
        repeat(count) { index ->
            val timestamp = System.currentTimeMillis()
            val imageId = ImageUploadConstants.Formats.IMAGE_ID_FORMAT.format(timestamp, index)
            val result = repository.uploadImage(imageId)
            emit(result)
        }
    }
}
