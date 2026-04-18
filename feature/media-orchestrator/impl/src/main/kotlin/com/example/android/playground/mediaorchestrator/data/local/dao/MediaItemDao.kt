package com.example.android.playground.mediaorchestrator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.playground.mediaorchestrator.data.local.entity.MediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemDao {
    @Query("SELECT * FROM media_items ORDER BY created_at ASC")
    fun observeAll(): Flow<List<MediaItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MediaItemEntity>)

    @Query("SELECT * FROM media_items WHERE upload_status = :status")
    suspend fun getByStatus(status: String): List<MediaItemEntity>

    @Query("UPDATE media_items SET upload_status = :status, remote_url = :remoteUrl WHERE id = :id")
    suspend fun updateStatus(
        id: String,
        status: String,
        remoteUrl: String?,
    )

    @Query("UPDATE media_items SET uploaded_chunks = :uploadedChunks WHERE id = :id")
    suspend fun updateChunkProgress(
        id: String,
        uploadedChunks: Int,
    )

    @Query("SELECT id FROM media_items WHERE upload_status = :status")
    suspend fun getIdsByStatus(status: String): List<String>

    @Query("DELETE FROM media_items")
    suspend fun deleteAll()
}
