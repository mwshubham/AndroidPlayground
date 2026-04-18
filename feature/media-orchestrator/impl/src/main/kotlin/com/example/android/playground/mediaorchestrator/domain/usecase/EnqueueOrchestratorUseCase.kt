package com.example.android.playground.mediaorchestrator.domain.usecase

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.android.playground.mediaorchestrator.data.worker.MediaUploadOrchestratorWorker
import javax.inject.Inject

class EnqueueOrchestratorUseCase
    @Inject
    constructor(private val workManager: WorkManager) {
        operator fun invoke() {
            val constraints =
                Constraints
                    .Builder()
                    // Worker will only start (or resume) when the device has an active
                    // network connection. If connectivity drops mid-upload the worker is
                    // paused by WorkManager and automatically retried when it returns.
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val request =
                OneTimeWorkRequestBuilder<MediaUploadOrchestratorWorker>()
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setConstraints(constraints)
                    .addTag(MediaUploadOrchestratorWorker.WORK_TAG)
                    .build()

            workManager.enqueueUniqueWork(
                uniqueWorkName = MediaUploadOrchestratorWorker.WORK_NAME,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = request,
            )
        }
    }
