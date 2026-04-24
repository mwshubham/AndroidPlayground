package com.example.android.playground.userinitiatedservice.data

import com.example.android.playground.common.TimingConstants

object TransferConstants {
    /** Unique job ID for JobScheduler (must not conflict with other jobs in the app). */
    const val JOB_ID = 2001

    /** Unique work name for WorkManager enqueueUniqueWork on API < 34. */
    const val WORK_NAME = "user_initiated_transfer"

    // Foreground-service notification constants (API < 34 WorkManager fallback path only)
    const val NOTIFICATION_ID = 2001
    const val CHANNEL_ID = "transfer_channel"
    const val CHANNEL_NAME = "File Transfer"

    // Shared simulated chunk delay so upload and transfer demos progress at the same pace.
    const val CHUNK_DELAY_MS = TimingConstants.FILE_TRANSFER_CHUNK_DELAY_MS
}
