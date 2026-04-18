package com.example.android.playground.userinitiatedservice.domain.repository

import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import kotlinx.coroutines.flow.Flow

interface TransferRepository {
    /** Live stream of all transfer items ordered by creation time. */
    fun observeAll(): Flow<List<TransferItem>>

    suspend fun insertAll(items: List<TransferItem>)

    /** Items currently waiting to start (state = PENDING). */
    suspend fun getPendingItems(): List<TransferItem>

    /** Items actively being transferred (state = RUNNING). Called from onStopJob. */
    suspend fun getRunningItems(): List<TransferItem>

    suspend fun updateStatus(id: String, status: TransferStatus)

    suspend fun updateProgress(id: String, uploadedChunks: Int)

    suspend fun clearAll()
}
