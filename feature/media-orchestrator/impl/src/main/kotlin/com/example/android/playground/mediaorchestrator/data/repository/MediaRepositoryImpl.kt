package com.example.android.playground.mediaorchestrator.data.repository

import com.example.android.playground.mediaorchestrator.data.local.dao.MediaItemDao
import com.example.android.playground.mediaorchestrator.data.local.entity.MediaItemEntity
import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl
    @Inject
    constructor(private val dao: MediaItemDao) : MediaRepository {
        override fun observeAll(): Flow<List<MediaItem>> =
            dao.observeAll().map { entities -> entities.map { it.toDomain() } }

        override suspend fun insertAll(items: List<MediaItem>) {
            dao.insertAll(items.map { it.toEntity() })
        }

        override suspend fun getPendingItems(): List<MediaItem> =
            dao.getByStatus(UploadStatus.PENDING.name).map { it.toDomain() }

        override suspend fun updateStatus(
            id: String,
            status: UploadStatus,
            remoteUrl: String?,
        ) {
            dao.updateStatus(id = id, status = status.name, remoteUrl = remoteUrl)
        }

        override suspend fun updateChunkProgress(
            id: String,
            uploadedChunks: Int,
        ) {
            dao.updateChunkProgress(id = id, uploadedChunks = uploadedChunks)
        }

        override suspend fun getSuccessfulIds(): List<String> = dao.getIdsByStatus(UploadStatus.SUCCESS.name)

        override suspend fun clearAll() {
            dao.deleteAll()
        }

        private fun MediaItemEntity.toDomain(): MediaItem =
            MediaItem(
                id = id,
                name = name,
                sizeBytes = sizeBytes,
                uploadStatus = UploadStatus.valueOf(uploadStatus),
                totalChunks = totalChunks,
                uploadedChunks = uploadedChunks,
                remoteUrl = remoteUrl,
                createdAt = createdAt,
            )

        private fun MediaItem.toEntity(): MediaItemEntity =
            MediaItemEntity(
                id = id,
                name = name,
                sizeBytes = sizeBytes,
                uploadStatus = uploadStatus.name,
                totalChunks = totalChunks,
                uploadedChunks = uploadedChunks,
                remoteUrl = remoteUrl,
                createdAt = createdAt,
            )
    }
