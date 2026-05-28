package com.example.android.playground.mediaorchestrator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.android.playground.mediaorchestrator.data.worker.MediaUploadOrchestratorWorker
import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.domain.usecase.AddMediaItemsUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.ClearMediaUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.EnqueueOrchestratorUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.GetMediaItemsUseCase
import com.example.android.playground.mediaorchestrator.presentation.intent.MediaOrchestratorIntent
import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel
import com.example.android.playground.mediaorchestrator.presentation.sideeffect.MediaOrchestratorSideEffect
import com.example.android.playground.mediaorchestrator.presentation.state.MediaOrchestratorState
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PICK_COUNT = 8
private const val ENQUEUE_DELAY_MS = 2_000L
private const val BYTES_PER_MB = 1_000_000L
private const val BYTES_PER_KB = 1_000L

@HiltViewModel
class MediaOrchestratorViewModel
    @Inject
    constructor(
        private val getMediaItemsUseCase: GetMediaItemsUseCase,
        private val addMediaItemsUseCase: AddMediaItemsUseCase,
        private val enqueueOrchestratorUseCase: EnqueueOrchestratorUseCase,
        private val clearMediaUseCase: ClearMediaUseCase,
        private val workManager: WorkManager,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MediaOrchestratorState())
        val state: StateFlow<MediaOrchestratorState> = _state.asStateFlow()

        private val _sideEffect = Channel<MediaOrchestratorSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            observeItems()
            observeWorkerStatus()
        }

        fun handleIntent(intent: MediaOrchestratorIntent) {
            when (intent) {
                is MediaOrchestratorIntent.PickAndEnqueue -> pickAndEnqueue()
                is MediaOrchestratorIntent.ClearAll -> clearAll()
                is MediaOrchestratorIntent.NavigateBack -> navigateBack()
            }
        }

        private fun observeItems() {
            viewModelScope.launch {
                getMediaItemsUseCase().collect { items ->
                    _state.update { current ->
                        current.copy(
                            items = items.map { it.toUiModel() },
                            totalCount = items.size,
                            successCount = items.count { it.uploadStatus == UploadStatus.SUCCESS },
                            failedCount = items.count { it.uploadStatus == UploadStatus.FAILED },
                            pendingCount = items.count { it.uploadStatus == UploadStatus.PENDING },
                            inProgressCount = items.count { it.uploadStatus == UploadStatus.IN_PROGRESS },
                            overallProgress = computeOverallProgress(items),
                        )
                    }
                }
            }
        }

        private fun observeWorkerStatus() {
            viewModelScope.launch {
                workManager
                    .getWorkInfosForUniqueWorkFlow(MediaUploadOrchestratorWorker.WORK_NAME)
                    .collect { workInfoList ->
                        val workerStatus =
                            when {
                                workInfoList.any { it.state == WorkInfo.State.RUNNING } -> WorkerStatus.RUNNING
                                workInfoList.any {
                                    it.state == WorkInfo.State.SUCCEEDED ||
                                        it.state == WorkInfo.State.FAILED ||
                                        it.state == WorkInfo.State.CANCELLED
                                } -> WorkerStatus.DONE
                                else -> WorkerStatus.IDLE
                            }
                        _state.update { it.copy(workerStatus = workerStatus) }
                    }
            }
        }

        private fun pickAndEnqueue() {
            if (_state.value.workerStatus == WorkerStatus.RUNNING) return
            viewModelScope.launch {
                // Optimistic insert — items appear in UI immediately in PENDING state
                // before any upload begins. This is the key UX principle.
                addMediaItemsUseCase(PICK_COUNT)
                delay(ENQUEUE_DELAY_MS)
                enqueueOrchestratorUseCase()
                _sideEffect.send(MediaOrchestratorSideEffect.ShowMessage("$PICK_COUNT media items queued for upload"))
            }
        }

        private fun clearAll() {
            viewModelScope.launch {
                clearMediaUseCase()
                _state.update { MediaOrchestratorState() }
                _sideEffect.send(MediaOrchestratorSideEffect.ShowMessage("Cleared all media and cancelled upload"))
            }
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(MediaOrchestratorSideEffect.NavigateBack)
            }
        }

        private fun computeOverallProgress(items: List<MediaItem>): Float {
            if (items.isEmpty()) return 0f
            val totalChunks = items.sumOf { it.totalChunks }.toFloat()
            val uploadedChunks = items.sumOf { it.uploadedChunks }.toFloat()
            return if (totalChunks > 0) uploadedChunks / totalChunks else 0f
        }

        private fun MediaItem.toUiModel(): MediaItemUiModel =
            MediaItemUiModel(
                id = id,
                name = name,
                sizeDisplay = formatBytes(sizeBytes),
                status = uploadStatus,
                progress = if (totalChunks > 0) uploadedChunks.toFloat() / totalChunks else 0f,
                remoteUrl = remoteUrl,
            )

        private fun formatBytes(bytes: Long): String =
            when {
                bytes >= BYTES_PER_MB -> "${bytes / BYTES_PER_MB} MB"
                bytes >= BYTES_PER_KB -> "${bytes / BYTES_PER_KB} KB"
                else -> "$bytes B"
            }
    }
