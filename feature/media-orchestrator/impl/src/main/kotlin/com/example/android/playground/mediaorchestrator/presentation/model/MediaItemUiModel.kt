package com.example.android.playground.mediaorchestrator.presentation.model

import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus

data class MediaItemUiModel(
    val id: String,
    val name: String,
    val sizeDisplay: String,
    val status: UploadStatus,
    val progress: Float,
    val remoteUrl: String?,
)
