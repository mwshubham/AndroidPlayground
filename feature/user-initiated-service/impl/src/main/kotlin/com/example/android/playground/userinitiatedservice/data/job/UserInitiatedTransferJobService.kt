package com.example.android.playground.userinitiatedservice.data.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.android.playground.userinitiatedservice.data.TransferConstants
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * User-Initiated Data Transfer Job Service (API 34+).
 *
 * This JobService is the API 34+ counterpart to
 * [com.example.android.playground.userinitiatedservice.data.worker.TransferWorkerLegacy].
 * It is scheduled via
 * JobScheduler.setUserInitiated(true) and has two important OS-level behaviours that
 * distinguish it from a regular foreground service or WorkManager job:
 *
 * 1. Task Manager visibility — the job appears in the system Task Manager (long-press Recents
 *    on Android 14+). Users can monitor progress and stop the job there instead of via a
 *    notification. Tapping "Stop" calls [onStopJob].
 *
 * 2. Extended grace period — the OS allows ~10 minutes of execution before enforcing any
 *    visibility constraints, compared to 10 seconds for a regular foreground service. This
 *    means short transfers finish silently without ever showing a notification.
 *
 * 3. Gesture enforcement — JobScheduler validates that schedule() was called within a
 *    user-gesture call stack. If called from a background component (alarm, broadcast, etc.)
 *    it throws SecurityException at schedule time, making silent background misuse impossible.
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@AndroidEntryPoint
class UserInitiatedTransferJobService : JobService() {
    @Inject
    lateinit var repository: TransferRepository

    /**
     * SupervisorJob so a failed child coroutine (single item) does not cancel siblings.
     * The scope is cancelled in onDestroy when the service is finally torn down.
     */
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var transferJob: Job? = null

    companion object {
        private const val TAG = "UIJTransferJobService"
    }

    /**
     * Called by the system when it is ready to run the job.
     * Must return true because the work continues asynchronously in a coroutine.
     */
    override fun onStartJob(params: JobParameters): Boolean {
        Timber.tag(TAG).i("onStartJob — scheduling transfer coroutine")
        transferJob =
            serviceScope.launch {
                runTransfer(params)
            }
        return true
    }

    /**
     * Called by the system when it needs to stop the job — most commonly because the user
     * tapped "Stop" in the Task Manager, or the required network condition was lost.
     *
     * Returning true signals that we want the job rescheduled when conditions improve.
     * We mark RUNNING items as CANCELLED so the UI accurately reflects the interruption.
     */
    override fun onStopJob(params: JobParameters): Boolean {
        Timber.tag(TAG).w("onStopJob — job stopped by system/user; transferJob cancelling")
        transferJob?.cancel()

        // Update DB: mark any still-RUNNING items as CANCELLED so the UI shows the right state.
        // We use serviceScope (not the now-cancelled transferJob scope) for this cleanup write.
        serviceScope.launch {
            val running = repository.getRunningItems()
            running.forEach { repository.updateStatus(it.id, TransferStatus.CANCELLED) }
            Timber.tag(TAG).i("onStopJob — marked ${running.size} items as CANCELLED")
        }
        return true // want rescheduled
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Timber.tag(TAG).i("onDestroy — service scope cancelled")
    }

    /**
     * Transfers all PENDING items sequentially, writing chunk progress to the DB after
     * every simulated chunk. This means the UI sees real-time progress via the Room Flow.
     */
    private suspend fun runTransfer(params: JobParameters) {
        val pending = repository.getPendingItems()
        Timber.tag(TAG).i("runTransfer — ${pending.size} items to transfer")

        pending.forEach { item ->
            Timber.tag(TAG).d("[${item.name}] → RUNNING")
            repository.updateStatus(item.id, TransferStatus.RUNNING)

            for (chunk in 0 until item.totalChunks) {
                delay(TransferConstants.CHUNK_DELAY_MS)
                repository.updateProgress(item.id, chunk + 1)
                Timber.tag(TAG).v("[${item.name}] chunk ${chunk + 1}/${item.totalChunks} complete")
            }

            repository.updateStatus(item.id, TransferStatus.SUCCESS)
            Timber.tag(TAG).i("[${item.name}] → SUCCESS")
        }

        Timber.tag(TAG).i("runTransfer — all items done, calling jobFinished(wantsReschedule=false)")
        // Tell the system we finished successfully; wantsReschedule=false means do not retry
        jobFinished(params, false)
    }
}
