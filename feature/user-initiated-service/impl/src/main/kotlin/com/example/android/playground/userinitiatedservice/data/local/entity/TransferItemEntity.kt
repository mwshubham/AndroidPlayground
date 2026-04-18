package com.example.android.playground.userinitiatedservice.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfer_items")
data class TransferItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sizeBytes: Long,
    /** Stored as the enum name string so schema stays readable without a TypeConverter. */
    val status: String,
    val totalChunks: Int,
    val uploadedChunks: Int,
    val createdAt: Long,
)
