package com.example.android.playground.imageupload.data.repository

import com.example.android.playground.imageupload.domain.model.ImageUploadResult
import com.example.android.playground.imageupload.domain.model.UploadStatus
import com.example.android.playground.imageupload.domain.repository.ImageUploadRepository
import com.example.android.playground.imageupload.util.ImageUploadConstants
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ImageUploadRepositoryImpl
    @Inject
    constructor() : ImageUploadRepository {
        override suspend fun uploadImage(imageId: String): ImageUploadResult {
            // Simulate network delay
            delay(
                Random.nextLong(
                    ImageUploadConstants.Upload.MIN_UPLOAD_DELAY_MS,
                    ImageUploadConstants.Upload.MAX_UPLOAD_DELAY_MS,
                ),
            )

            // Simulate success rate
            val isSuccess = Random.nextFloat() <= ImageUploadConstants.Upload.SUCCESS_RATE

            return if (isSuccess) {
                ImageUploadResult(
                    id = imageId,
                    url = ImageUploadConstants.Formats.IMAGE_URL_FORMAT.format(imageId),
                    status = UploadStatus.SUCCESS,
                )
            } else {
                ImageUploadResult(
                    id = imageId,
                    url = ImageUploadConstants.Formats.IMAGE_URL_FORMAT.format(imageId),
                    status = UploadStatus.FAILURE,
                    error = "Upload failed due to network error.",
                )
            }
        }
    }
