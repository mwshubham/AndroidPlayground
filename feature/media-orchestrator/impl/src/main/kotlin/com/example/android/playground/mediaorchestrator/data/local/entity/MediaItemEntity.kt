package com.example.android.playground.mediaorchestrator.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "size_bytes")
    val sizeBytes: Long,
    @ColumnInfo(name = "upload_status")
    val uploadStatus: String,
    @ColumnInfo(name = "total_chunks")
    val totalChunks: Int,
    @ColumnInfo(name = "uploaded_chunks")
    val uploadedChunks: Int,
    @ColumnInfo(name = "remote_url")
    val remoteUrl: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
