package com.example.android.systemdesign.imageupload.presentation

import com.example.android.systemdesign.imageupload.domain.model.ImageUploadResult

data class ImageUploadState(
    val isUploading: Boolean = false,
    val uploadResults: List<ImageUploadResult> = emptyList(),
    val successCount: Int = 0,
    val failureCount: Int = 0,
    val totalCount: Int = 0,
    val progress: Float = 0f
) {
    val isCompleted: Boolean = uploadResults.size == totalCount && totalCount > 0
}
