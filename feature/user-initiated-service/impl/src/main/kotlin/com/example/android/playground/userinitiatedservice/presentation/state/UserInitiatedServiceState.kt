package com.example.android.playground.userinitiatedservice.presentation.state

import android.os.Build
import com.example.android.playground.userinitiatedservice.presentation.model.TransferItemUiModel

/** Mirrors the job lifecycle for both the UIJ (API 34+) and WorkManager (API < 34) paths. */
enum class JobStatus { IDLE, RUNNING, DONE }

data class UserInitiatedServiceState(
    val items: List<TransferItemUiModel> = emptyList(),
    val jobStatus: JobStatus = JobStatus.IDLE,
    val totalCount: Int = 0,
    val successCount: Int = 0,
    val failedCount: Int = 0,
    val pendingCount: Int = 0,
    val runningCount: Int = 0,
    val cancelledCount: Int = 0,
    val overallProgress: Float = 0f,
    /** Drives the mode banner and comparison highlight without duplicate SDK checks in the UI. */
    val isApi34Plus: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
)
