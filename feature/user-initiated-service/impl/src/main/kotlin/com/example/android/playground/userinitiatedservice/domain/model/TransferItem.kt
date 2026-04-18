package com.example.android.playground.userinitiatedservice.domain.model

data class TransferItem(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val status: TransferStatus,
    val totalChunks: Int,
    val uploadedChunks: Int,
    val createdAt: Long,
)
