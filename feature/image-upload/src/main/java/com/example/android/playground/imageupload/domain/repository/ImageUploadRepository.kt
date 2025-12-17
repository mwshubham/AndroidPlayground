package com.example.android.playground.imageupload.domain.repository

import com.example.android.playground.imageupload.domain.model.ImageUploadResult

interface ImageUploadRepository {
    suspend fun uploadImage(imageId: String): ImageUploadResult
}
