package com.example.android.playground.userinitiatedservice.domain.usecase

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.android.playground.userinitiatedservice.data.TransferConstants
import com.example.android.playground.userinitiatedservice.data.job.UserInitiatedTransferJobService
import com.example.android.playground.userinitiatedservice.data.worker.TransferWorkerLegacy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Starts a file transfer using the most capable mechanism available on the current API level.
 *
 * API 34+ — User-Initiated Data Transfer Job (UIJ):
 *   - Scheduled via JobScheduler with setUserInitiated(true).
 *   - MUST be called within a user-gesture call stack — the OS enforces this and will throw
 *     SecurityException if the call originates from a background component.
 *   - The job is visible in the system Task Manager (long-press Recents) rather than in the
 *     notification shade, giving users a dedicated control surface to monitor or stop it.
 *   - The OS grants an extended grace period (~10 minutes) before enforcing visibility.
 *   - No foreground-service permission or POST_NOTIFICATIONS permission required.
 *
 * API < 34 — WorkManager Expedited fallback:
 *   - Uses WorkManager.enqueueUniqueWork with setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST).
 *   - Promotes to a DATA_SYNC foreground service, showing a notification in the shade.
 *   - Subject to the expedited-work quota allocated per app per day.
 */
class StartTransferUseCase
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val workManager: WorkManager,
        private val jobScheduler: JobScheduler,
    ) {
        operator fun invoke() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                scheduleUserInitiatedJob()
            } else {
                enqueueWorkerLegacy()
            }
        }

        /**
         * Schedules a User-Initiated Data Transfer Job.
         *
         * Key constraints set here:
         *  - setUserInitiated(true) — marks the job as user-initiated; JobScheduler enforces
         *    that this call happens within a user-gesture window (SecurityException otherwise).
         *  - setRequiredNetworkType(NETWORK_TYPE_ANY) — mandatory for UIJ; a network constraint
         *    must always be specified. Without it, schedule() throws IllegalArgumentException.
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        private fun scheduleUserInitiatedJob() {
            val jobInfo =
                JobInfo
                    .Builder(
                        // jobId =
                        TransferConstants.JOB_ID,
                        // jobService =
                        ComponentName(context, UserInitiatedTransferJobService::class.java),
                    ).setUserInitiated(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build()
            jobScheduler.schedule(jobInfo)
        }

        /**
         * WorkManager Expedited fallback for API < 34.
         *
         * RUN_AS_NON_EXPEDITED_WORK_REQUEST ensures the job runs even when the expedited
         * quota is exhausted — it simply falls back to a regular worker without crashing.
         */
        private fun enqueueWorkerLegacy() {
            val request =
                OneTimeWorkRequestBuilder<TransferWorkerLegacy>()
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .build()
            workManager.enqueueUniqueWork(
                uniqueWorkName = TransferConstants.WORK_NAME,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = request,
            )
        }
    }
