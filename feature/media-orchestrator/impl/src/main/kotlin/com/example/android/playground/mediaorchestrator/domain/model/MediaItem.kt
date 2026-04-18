package com.example.android.playground.mediaorchestrator.domain.model

data class MediaItem(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val uploadStatus: UploadStatus,
    val totalChunks: Int,
    val uploadedChunks: Int,
    val remoteUrl: String?,
    val createdAt: Long,
)
