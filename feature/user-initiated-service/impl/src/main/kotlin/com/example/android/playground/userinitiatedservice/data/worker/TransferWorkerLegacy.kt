package com.example.android.playground.userinitiatedservice.data.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.android.playground.userinitiatedservice.data.TransferConstants
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager CoroutineWorker used as a fallback on API < 34 where User-Initiated Jobs
 * are not available.
 *
 * Differences from [UserInitiatedTransferJobService]:
 *  - Promotes to a DATA_SYNC foreground service → shows a progress notification in the
 *    notification shade (not in Task Manager).
 *  - Subject to WorkManager's daily expedited-quota limit.
 *  - The 10-second foreground service grace period applies — if the notification is not
 *    shown within 10 seconds, the OS may stop the service (API 34+, but this path only
 *    runs on API < 34 so the stricter enforcement is not triggered here).
 *  - No user-gesture enforcement: the worker can be enqueued from any context (boot
 *    receiver, alarm, etc.). This is the key safety gap that UIJ was designed to close.
 */
@HiltWorker
class TransferWorkerLegacy
    @AssistedInject
    constructor(
        @Assisted private val appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val repository: TransferRepository,
    ) : CoroutineWorker(appContext, workerParams) {

        companion object {
            private const val TAG = "TransferWorkerLegacy"
        }

        override suspend fun getForegroundInfo(): ForegroundInfo = createForegroundInfo()

        override suspend fun doWork(): Result {
            Log.i(TAG, "doWork() — legacy WorkManager Expedited path (API < 34)")

            // Promote to foreground service so the work survives when the app goes to background.
            // Wrapped in try/catch because setForeground() can throw IllegalStateException
            // (wrapping BackgroundServiceStartNotAllowedException) when the expedited-quota
            // is exhausted AND the app is already in the background.
            try {
                setForeground(createForegroundInfo())
            } catch (e: IllegalStateException) {
                Log.w(TAG, "Cannot promote to FGS — continuing as background worker", e)
            }

            val pending = repository.getPendingItems()
            Log.i(TAG, "Pending items: ${pending.size}")

            pending.forEach { item ->
                Log.d(TAG, "[${item.name}] → RUNNING")
                repository.updateStatus(item.id, TransferStatus.RUNNING)

                for (chunk in 0 until item.totalChunks) {
                    kotlinx.coroutines.delay(TransferConstants.CHUNK_DELAY_MS)
                    repository.updateProgress(item.id, chunk + 1)
                    Log.v(TAG, "[${item.name}] chunk ${chunk + 1}/${item.totalChunks} complete")
                }

                repository.updateStatus(item.id, TransferStatus.SUCCESS)
                Log.i(TAG, "[${item.name}] → SUCCESS")
            }

            Log.i(TAG, "doWork() complete — returning Result.success()")
            return Result.success()
        }

        private fun createForegroundInfo(): ForegroundInfo {
            createNotificationChannel()
            val notification = buildNotification()
            return when {
                // DATA_SYNC is the appropriate foreground service type for file transfers (API 29+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    ForegroundInfo(
                        TransferConstants.NOTIFICATION_ID,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                    )
                // API 28 (minSdk) does not support foreground service type argument
                else ->
                    ForegroundInfo(TransferConstants.NOTIFICATION_ID, notification)
            }
        }

        private fun buildNotification(): Notification =
            NotificationCompat
                .Builder(appContext, TransferConstants.CHANNEL_ID)
                .setContentTitle("Transferring files")
                .setContentText("Your files are being transferred in the background")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setOngoing(true)
                .setSilent(true)
                .build()

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        TransferConstants.CHANNEL_ID,
                        TransferConstants.CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_LOW,
                    ).apply {
                        description = "Shows progress of background file transfers"
                    }
                val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(channel)
            }
        }
    }
