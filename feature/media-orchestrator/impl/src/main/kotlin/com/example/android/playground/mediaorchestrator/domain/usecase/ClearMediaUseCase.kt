package com.example.android.playground.mediaorchestrator.domain.usecase

import androidx.work.WorkManager
import com.example.android.playground.mediaorchestrator.data.worker.MediaUploadOrchestratorWorker
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import javax.inject.Inject

class ClearMediaUseCase
    @Inject
    constructor(
        private val repository: MediaRepository,
        private val workManager: WorkManager,
    ) {
        suspend operator fun invoke() {
            workManager.cancelUniqueWork(MediaUploadOrchestratorWorker.WORK_NAME)
            repository.clearAll()
        }
    }
