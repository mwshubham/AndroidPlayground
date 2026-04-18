package com.example.android.playground.mediaorchestrator.domain.repository

import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun observeAll(): Flow<List<MediaItem>>

    suspend fun insertAll(items: List<MediaItem>)

    suspend fun getPendingItems(): List<MediaItem>

    suspend fun updateStatus(
        id: String,
        status: UploadStatus,
        remoteUrl: String? = null,
    )

    suspend fun updateChunkProgress(
        id: String,
        uploadedChunks: Int,
    )

    suspend fun getSuccessfulIds(): List<String>

    suspend fun clearAll()
}
