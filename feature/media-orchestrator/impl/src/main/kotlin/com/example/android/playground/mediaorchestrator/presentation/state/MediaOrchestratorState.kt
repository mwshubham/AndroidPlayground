package com.example.android.playground.mediaorchestrator.presentation.state

import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel

enum class WorkerStatus { IDLE, RUNNING, DONE }

data class MediaOrchestratorState(
    val items: List<MediaItemUiModel> = emptyList(),
    val workerStatus: WorkerStatus = WorkerStatus.IDLE,
    val totalCount: Int = 0,
    val successCount: Int = 0,
    val failedCount: Int = 0,
    val pendingCount: Int = 0,
    val inProgressCount: Int = 0,
    val overallProgress: Float = 0f,
)
