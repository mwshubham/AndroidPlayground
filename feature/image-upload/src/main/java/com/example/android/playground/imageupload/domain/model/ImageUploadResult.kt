package com.example.android.playground.imageupload.domain.model

data class ImageUploadResult(
    val id: String,
    val url: String,
    val status: UploadStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val error: String? = null
)

enum class UploadStatus {
    PENDING,
    SUCCESS,
    FAILURE
}
