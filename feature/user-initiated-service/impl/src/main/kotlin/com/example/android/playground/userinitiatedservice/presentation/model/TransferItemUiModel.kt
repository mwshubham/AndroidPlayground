package com.example.android.playground.userinitiatedservice.presentation.model

import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus

data class TransferItemUiModel(
    val id: String,
    val name: String,
    val sizeDisplay: String,
    val status: TransferStatus,
    val progress: Float,
)
