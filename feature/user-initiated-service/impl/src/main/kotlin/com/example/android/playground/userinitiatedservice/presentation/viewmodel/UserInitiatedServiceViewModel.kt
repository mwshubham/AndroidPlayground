package com.example.android.playground.userinitiatedservice.presentation.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.usecase.AddTransferItemsUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.ClearTransfersUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.GetTransferItemsUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.StartTransferUseCase
import com.example.android.playground.userinitiatedservice.presentation.intent.UserInitiatedServiceIntent
import com.example.android.playground.userinitiatedservice.presentation.model.TransferItemUiModel
import com.example.android.playground.userinitiatedservice.presentation.sideeffect.UserInitiatedServiceSideEffect
import com.example.android.playground.userinitiatedservice.presentation.state.JobStatus
import com.example.android.playground.userinitiatedservice.presentation.state.UserInitiatedServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TRANSFER_COUNT = 5

@HiltViewModel
class UserInitiatedServiceViewModel
    @Inject
    constructor(
        private val getTransferItemsUseCase: GetTransferItemsUseCase,
        private val addTransferItemsUseCase: AddTransferItemsUseCase,
        private val startTransferUseCase: StartTransferUseCase,
        private val clearTransfersUseCase: ClearTransfersUseCase,
    ) : ViewModel() {

        private val _state = MutableStateFlow(UserInitiatedServiceState())
        val state: StateFlow<UserInitiatedServiceState> = _state.asStateFlow()

        private val _sideEffect = Channel<UserInitiatedServiceSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            observeItems()
        }

        fun handleIntent(intent: UserInitiatedServiceIntent) {
            when (intent) {
                is UserInitiatedServiceIntent.StartTransfer -> startTransfer()
                is UserInitiatedServiceIntent.ClearAll -> clearAll()
                is UserInitiatedServiceIntent.NavigateBack -> navigateBack()
            }
        }

        /**
         * Observes Room for all transfer items and derives job status directly from item states.
         * This works uniformly for both the UIJ (API 34+) and WorkManager (API < 34) paths
         * because both the JobService and Worker write status updates to Room.
         */
        private fun observeItems() {
            viewModelScope.launch {
                getTransferItemsUseCase().collect { items ->
                    val derivedStatus =
                        when {
                            items.any { it.status == TransferStatus.RUNNING } -> JobStatus.RUNNING
                            items.isNotEmpty() &&
                                items.none { it.status == TransferStatus.PENDING || it.status == TransferStatus.RUNNING } ->
                                JobStatus.DONE
                            else -> JobStatus.IDLE
                        }
                    _state.update { current ->
                        current.copy(
                            items = items.map { it.toUiModel() },
                            jobStatus = derivedStatus,
                            totalCount = items.size,
                            successCount = items.count { it.status == TransferStatus.SUCCESS },
                            failedCount = items.count { it.status == TransferStatus.FAILED },
                            pendingCount = items.count { it.status == TransferStatus.PENDING },
                            runningCount = items.count { it.status == TransferStatus.RUNNING },
                            cancelledCount = items.count { it.status == TransferStatus.CANCELLED },
                            overallProgress = computeProgress(items),
                        )
                    }
                }
            }
        }

        private fun startTransfer() {
            if (_state.value.jobStatus == JobStatus.RUNNING) return
            viewModelScope.launch {
                // Insert items into DB first so they appear in the UI immediately (PENDING state).
                // The OS user-activity window for UIJ is ~10 seconds; a Room insert takes <50 ms,
                // so startTransferUseCase() is still called well within the gesture window.
                addTransferItemsUseCase(TRANSFER_COUNT)
                startTransferUseCase()

                val mode =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        "User-Initiated Job (API 34+)"
                    } else {
                        "WorkManager Expedited fallback"
                    }
                _sideEffect.send(
                    UserInitiatedServiceSideEffect.ShowMessage(
                        "$TRANSFER_COUNT files queued via $mode",
                    ),
                )
            }
        }

        private fun clearAll() {
            viewModelScope.launch {
                clearTransfersUseCase()
                _state.update { UserInitiatedServiceState() }
                _sideEffect.send(UserInitiatedServiceSideEffect.ShowMessage("Cleared all transfers"))
            }
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(UserInitiatedServiceSideEffect.NavigateBack)
            }
        }

        private fun computeProgress(items: List<TransferItem>): Float {
            if (items.isEmpty()) return 0f
            val total = items.sumOf { it.totalChunks }.toFloat()
            val done = items.sumOf { it.uploadedChunks }.toFloat()
            return if (total > 0f) done / total else 0f
        }

        private fun TransferItem.toUiModel(): TransferItemUiModel =
            TransferItemUiModel(
                id = id,
                name = name,
                sizeDisplay = formatBytes(sizeBytes),
                status = status,
                progress = if (totalChunks > 0) uploadedChunks.toFloat() / totalChunks else 0f,
            )

        private fun formatBytes(bytes: Long): String =
            when {
                bytes >= 1_000_000 -> "${bytes / 1_000_000} MB"
                bytes >= 1_000 -> "${bytes / 1_000} KB"
                else -> "$bytes B"
            }
    }
