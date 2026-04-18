package com.example.android.playground.userinitiatedservice.data

object TransferConstants {
    /** Unique job ID for JobScheduler (must not conflict with other jobs in the app). */
    const val JOB_ID = 2001

    /** Unique work name for WorkManager enqueueUniqueWork on API < 34. */
    const val WORK_NAME = "user_initiated_transfer"

    // Foreground-service notification constants (API < 34 WorkManager fallback path only)
    const val NOTIFICATION_ID = 2001
    const val CHANNEL_ID = "transfer_channel"
    const val CHANNEL_NAME = "File Transfer"

    // Simulated chunk delay — 800 ms per chunk gives visible progress in the UI
    const val CHUNK_DELAY_MS = 2_000L
}
