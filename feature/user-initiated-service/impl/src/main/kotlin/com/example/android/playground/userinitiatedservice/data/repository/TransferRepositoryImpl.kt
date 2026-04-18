package com.example.android.playground.userinitiatedservice.data.repository

import com.example.android.playground.userinitiatedservice.data.local.dao.TransferItemDao
import com.example.android.playground.userinitiatedservice.data.local.entity.TransferItemEntity
import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransferRepositoryImpl
    @Inject
    constructor(
        private val dao: TransferItemDao,
    ) : TransferRepository {
        override fun observeAll(): Flow<List<TransferItem>> =
            dao.observeAll().map { entities -> entities.map { it.toDomain() } }

        override suspend fun insertAll(items: List<TransferItem>) =
            dao.insertAll(items.map { it.toEntity() })

        override suspend fun getPendingItems(): List<TransferItem> =
            dao.getByStatus(listOf(TransferStatus.PENDING.name)).map { it.toDomain() }

        override suspend fun getRunningItems(): List<TransferItem> =
            dao.getByStatus(listOf(TransferStatus.RUNNING.name)).map { it.toDomain() }

        override suspend fun updateStatus(
            id: String,
            status: TransferStatus,
        ) = dao.updateStatus(id, status.name)

        override suspend fun updateProgress(
            id: String,
            uploadedChunks: Int,
        ) = dao.updateProgress(id, uploadedChunks)

        override suspend fun clearAll() = dao.deleteAll()

        // --- Mapping ---

        private fun TransferItemEntity.toDomain(): TransferItem =
            TransferItem(
                id = id,
                name = name,
                sizeBytes = sizeBytes,
                status = TransferStatus.valueOf(status),
                totalChunks = totalChunks,
                uploadedChunks = uploadedChunks,
                createdAt = createdAt,
            )

        private fun TransferItem.toEntity(): TransferItemEntity =
            TransferItemEntity(
                id = id,
                name = name,
                sizeBytes = sizeBytes,
                status = status.name,
                totalChunks = totalChunks,
                uploadedChunks = uploadedChunks,
                createdAt = createdAt,
            )
    }
