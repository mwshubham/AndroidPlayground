package com.example.android.playground.mediaorchestrator.data.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.android.playground.common.TimingConstants
import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import timber.log.Timber
import kotlin.random.Random

/**
 * Single orchestrator worker for all pending media uploads.
 *
 * Key design decisions:
 *  - Single worker (vs per-item workers) to avoid WorkManager chain-cancellation issues
 *  - coroutine Semaphore(3) caps concurrent uploads — no TCP saturation
 *  - DB is the only state store — chunk progress written after every chunk so resumption
 *    is possible if the process is killed and WorkManager re-schedules the job
 *  - Uses mediaProcessing foreground service type (API 35+) with DATA_SYNC fallback (API 29+)
 *    so the OS grants the process up to 6 hours of guaranteed runtime
 *  - Returns Result.success() even when some items fail — the DB records their FAILED status
 *    and the UI can offer a retry. Returning failure() would trigger WorkManager retries
 *    of the entire batch which is unnecessary here.
 */
@HiltWorker
class MediaUploadOrchestratorWorker
    @AssistedInject
    constructor(
        @Assisted private val appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val repository: MediaRepository,
    ) : CoroutineWorker(appContext, workerParams) {
        companion object {
            const val WORK_NAME = "media_upload_orchestrator"
            const val WORK_TAG = "media_upload"

            private const val NOTIFICATION_ID = 1001
            private const val CHANNEL_ID = "media_upload_channel"
            private const val CHANNEL_NAME = "Media Upload"
            private const val TAG = "MediaOrchestratorWorker"

            // Simulated upload parameters
            private const val MAX_CONCURRENT_UPLOADS = 3
            private const val FAILURE_RATE = 0.15 // 15% chance of failure per item (first attempt)
        }

        override suspend fun getForegroundInfo(): ForegroundInfo = createForegroundInfo()

        override suspend fun doWork(): Result {
            val workStartTimeMs = SystemClock.elapsedRealtime()
            Timber.tag(TAG).i("doWork() started — runAttemptCount=$runAttemptCount")

            // setForeground() can throw IllegalStateException (wrapping
            // BackgroundServiceStartNotAllowedException) when:
            //   - The work fell back to non-expedited mode (quota exhausted)
            //   - AND the app process is already in the background
            // In that case we log and continue without a foreground service.
            // The OS will still run the worker but with a shorter execution window.
            try {
                setForeground(createForegroundInfo())
            } catch (e: IllegalStateException) {
                Timber.tag(TAG).w(
                    e,
                    "Cannot promote to foreground service — app is in background and " +
                        "expedited quota was exhausted. Continuing as background worker.",
                )
            }

            val pendingItems = repository.getPendingItems()
            Timber.tag(TAG).i("Pending items to upload: ${pendingItems.size}")

            if (pendingItems.isEmpty()) {
                Timber.tag(TAG).i("No pending items — finishing immediately | durationMs=${elapsedSince(workStartTimeMs)}")
                return Result.success()
            }

            // Upload with bounded concurrency — max MAX_CONCURRENT_UPLOADS at the same time
            Timber.tag(TAG).i("Starting concurrent uploads (maxConcurrent=$MAX_CONCURRENT_UPLOADS)")
            val semaphore = Semaphore(MAX_CONCURRENT_UPLOADS)
            coroutineScope {
                pendingItems.forEach { item ->
                    launch {
                        semaphore.withPermit {
                            uploadItemWithChunkResume(item)
                        }
                    }
                }
            }
            Timber.tag(TAG).i("All coroutines finished — all items processed")

            // Simulate batch-associate call to the domain API after all uploads finish.
            // In a real app this would be: POST /posts/{id} { mediaIds: [...] }
            val successfulIds = repository.getSuccessfulIds()
            Timber.tag(TAG).i("Successful uploads: ${successfulIds.size} / ${pendingItems.size}")
            if (successfulIds.isNotEmpty()) {
                simulateBatchAssociate(successfulIds)
            }

            Timber.tag(TAG).i("doWork() complete — returning Result.success() | durationMs=${elapsedSince(workStartTimeMs)}")
            return Result.success()
        }

        /**
         * Uploads a single item chunk by chunk, writing progress to DB after every chunk.
         * This means if the process is killed at chunk 3/5, the worker will resume from
         * chunk 4 when WorkManager re-schedules — zero re-upload of completed chunks.
         */
        private suspend fun uploadItemWithChunkResume(item: MediaItem) {
            val itemStartTimeMs = SystemClock.elapsedRealtime()
            Timber.tag(TAG).d("[${item.name}] Starting upload (id=${item.id.take(8)}…)")

            // Simulate random failure (15% chance) to demonstrate FAILED state in UI
            if (Random.nextDouble() < FAILURE_RATE) {
                Timber.tag(TAG).w("[${item.name}] Simulated failure — marking FAILED")
                repository.updateStatus(id = item.id, status = UploadStatus.FAILED)
                return
            }

            repository.updateStatus(id = item.id, status = UploadStatus.IN_PROGRESS)
            Timber.tag(TAG).d("[${item.name}] Status → IN_PROGRESS")

            // Read checkpoint from DB — resume from next chunk if previously interrupted
            val startChunk = item.uploadedChunks
            if (startChunk > 0) {
                Timber.tag(TAG).i("[${item.name}] Resuming from chunk $startChunk / ${item.totalChunks} (process was killed previously)")
            }

            for (chunk in startChunk until item.totalChunks) {
                if (shouldLogChunkProgress(chunk = chunk, totalChunks = item.totalChunks)) {
                    Timber.tag(TAG).v("[${item.name}] Uploading chunk ${chunk + 1} / ${item.totalChunks}")
                }
                // Simulate chunk upload (network I/O in a real app)
                delay(TimingConstants.FILE_TRANSFER_CHUNK_DELAY_MS)

                // Checkpoint: write progress to DB immediately after each chunk.
                // If process dies here, next run reads this value and resumes from chunk+1
                repository.updateChunkProgress(id = item.id, uploadedChunks = chunk + 1)
                if (shouldLogChunkProgress(chunk = chunk, totalChunks = item.totalChunks)) {
                    Timber.tag(TAG).v("[${item.name}] Chunk ${chunk + 1} committed to DB")
                }
            }

            val fakeCdnUrl = "https://cdn.example.com/media/${item.id}.${item.name.substringAfterLast('.')}"
            repository.updateStatus(
                id = item.id,
                status = UploadStatus.SUCCESS,
                remoteUrl = fakeCdnUrl,
            )
            Timber.tag(TAG).i(
                "[${item.name}] Upload complete — Status → SUCCESS | cdnUrl=$fakeCdnUrl | durationMs=${elapsedSince(itemStartTimeMs)}",
            )
        }

        /**
         * Simulates the final batch-associate API call (e.g., PATCH /posts/{id}).
         * In production: one atomic call to attach all successfully uploaded media to the entity.
         */
        private suspend fun simulateBatchAssociate(mediaIds: List<String>) {
            Timber.tag(TAG).i("BatchAssociate → PATCH /posts/{id} with ${mediaIds.size} mediaIds: $mediaIds")
            delay(TimingConstants.BATCH_ASSOCIATION_DELAY_MS) // simulate lightweight JSON POST
            Timber.tag(TAG).i("BatchAssociate complete — all media attached to entity")
        }

        private fun shouldLogChunkProgress(
            chunk: Int,
            totalChunks: Int,
        ): Boolean {
            val chunkNumber = chunk + 1
            return chunkNumber == 1 ||
                chunkNumber == totalChunks ||
                chunkNumber % TimingConstants.PROGRESS_LOG_SAMPLE_INTERVAL == 0
        }

        private fun elapsedSince(startTimeMs: Long): Long = SystemClock.elapsedRealtime() - startTimeMs

        private fun createForegroundInfo(): ForegroundInfo {
            createNotificationChannel()
            val notification = buildNotification()

            val type = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM -> "MEDIA_PROCESSING (API 35+)"
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> "DATA_SYNC (API 29+)"
                else -> "none (API 28 minSdk)"
            }
            Timber.tag(TAG).d("createForegroundInfo() — foregroundServiceType=$type")

            return when {
                // API 35+ (Android 15): mediaProcessing type — designed for upload/processing tasks
                // OS grants up to 6 hours of guaranteed runtime
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM ->
                    ForegroundInfo(
                        /* notificationId = */ NOTIFICATION_ID,
                        /* notification = */ notification,
                        /* foregroundServiceType = */ ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING,
                    )

                // API 29+ (Android 10): use dataSync as the closest applicable type
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    ForegroundInfo(
                        /* notificationId = */ NOTIFICATION_ID,
                        /* notification = */ notification,
                        /* foregroundServiceType = */ ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                    )

                // API 28 (minSdk): no foreground service type argument
                else -> ForegroundInfo(NOTIFICATION_ID, notification)
            }
        }

        private fun buildNotification(): Notification =
            NotificationCompat
                .Builder(appContext, CHANNEL_ID)
                .setContentTitle("Uploading media")
                .setContentText("Your media is being uploaded in the background")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setOngoing(true)
                .setSilent(true)
                .build()

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_LOW,
                    ).apply {
                        description = "Shows progress of background media uploads"
                    }
                val notificationManager =
                    appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
