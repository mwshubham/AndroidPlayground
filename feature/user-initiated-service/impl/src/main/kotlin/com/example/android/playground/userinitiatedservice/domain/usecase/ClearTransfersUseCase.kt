package com.example.android.playground.userinitiatedservice.domain.usecase

import android.app.job.JobScheduler
import android.os.Build
import androidx.work.WorkManager
import com.example.android.playground.userinitiatedservice.data.TransferConstants
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import javax.inject.Inject

class ClearTransfersUseCase
    @Inject
    constructor(
        private val workManager: WorkManager,
        private val jobScheduler: JobScheduler,
        private val repository: TransferRepository,
    ) {
        suspend operator fun invoke() {
            // Cancel the running job/worker first so it stops writing to DB
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                jobScheduler.cancel(TransferConstants.JOB_ID)
            } else {
                workManager.cancelUniqueWork(TransferConstants.WORK_NAME)
            }
            // Then clear the DB — repository is the single source of truth
            repository.clearAll()
        }
    }
