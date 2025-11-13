package com.example.android.systemdesign.imageupload.domain.repository

import com.example.android.systemdesign.imageupload.domain.model.ImageUploadResult

interface ImageUploadRepository {
    suspend fun uploadImage(imageId: String): ImageUploadResult
}
