package com.example.android.playground.userinitiatedservice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.playground.userinitiatedservice.data.local.entity.TransferItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferItemDao {
    @Query("SELECT * FROM transfer_items ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<TransferItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TransferItemEntity>)

    @Query("SELECT * FROM transfer_items WHERE status IN (:statuses)")
    suspend fun getByStatus(statuses: List<String>): List<TransferItemEntity>

    @Query("UPDATE transfer_items SET status = :status WHERE id = :id")
    suspend fun updateStatus(
        id: String,
        status: String,
    )

    @Query("UPDATE transfer_items SET uploadedChunks = :uploadedChunks WHERE id = :id")
    suspend fun updateProgress(
        id: String,
        uploadedChunks: Int,
    )

    @Query("DELETE FROM transfer_items")
    suspend fun deleteAll()
}
