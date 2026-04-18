package com.example.android.playground.userinitiatedservice.domain.model

/**
 * Upload/transfer lifecycle states for a single file item.
 *
 * CANCELLED is unique to API 34+ UIJ: it is set when onStopJob() fires because the
 * user tapped "Stop" in the OS Task Manager (long-press Recents). On API < 34
 * (WorkManager path), clearing all items via ClearTransfersUseCase deletes the DB rows
 * before they can reach CANCELLED — so in practice CANCELLED is only visible on API 34+.
 */
enum class TransferStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    CANCELLED,
}
