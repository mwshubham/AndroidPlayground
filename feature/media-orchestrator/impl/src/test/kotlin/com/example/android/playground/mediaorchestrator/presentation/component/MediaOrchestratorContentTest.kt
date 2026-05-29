package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel
import com.example.android.playground.mediaorchestrator.presentation.state.MediaOrchestratorState
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
import org.junit.Rule
import org.junit.Test

class MediaOrchestratorContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                MediaOrchestratorContent(state = MediaOrchestratorState())
            }
        }
    }

    @Test
    fun runningWithItems() {
        paparazzi.snapshot {
            AppTheme {
                MediaOrchestratorContent(
                    state =
                        MediaOrchestratorState(
                            workerStatus = WorkerStatus.RUNNING,
                            totalCount = 3,
                            successCount = 1,
                            failedCount = 0,
                            pendingCount = 1,
                            inProgressCount = 1,
                            overallProgress = 0.4f,
                            items =
                                listOf(
                                    MediaItemUiModel(
                                        id = "1",
                                        name = "photo_001.jpg",
                                        sizeDisplay = "2.1 MB",
                                        status = UploadStatus.SUCCESS,
                                        progress = 1f,
                                        remoteUrl = "https://example.com/photo_001.jpg",
                                    ),
                                    MediaItemUiModel(
                                        id = "2",
                                        name = "photo_002.jpg",
                                        sizeDisplay = "1.8 MB",
                                        status = UploadStatus.IN_PROGRESS,
                                        progress = 0.6f,
                                        remoteUrl = null,
                                    ),
                                    MediaItemUiModel(
                                        id = "3",
                                        name = "photo_003.jpg",
                                        sizeDisplay = "3.4 MB",
                                        status = UploadStatus.PENDING,
                                        progress = 0f,
                                        remoteUrl = null,
                                    ),
                                ),
                        ),
                )
            }
        }
    }

    @Test
    fun doneWithFailure() {
        paparazzi.snapshot {
            AppTheme {
                MediaOrchestratorContent(
                    state =
                        MediaOrchestratorState(
                            workerStatus = WorkerStatus.DONE,
                            totalCount = 2,
                            successCount = 1,
                            failedCount = 1,
                            pendingCount = 0,
                            inProgressCount = 0,
                            overallProgress = 1f,
                            items =
                                listOf(
                                    MediaItemUiModel(
                                        id = "1",
                                        name = "photo_001.jpg",
                                        sizeDisplay = "2.1 MB",
                                        status = UploadStatus.SUCCESS,
                                        progress = 1f,
                                        remoteUrl = "https://example.com/photo_001.jpg",
                                    ),
                                    MediaItemUiModel(
                                        id = "2",
                                        name = "photo_002.jpg",
                                        sizeDisplay = "1.8 MB",
                                        status = UploadStatus.FAILED,
                                        progress = 0f,
                                        remoteUrl = null,
                                    ),
                                ),
                        ),
                )
            }
        }
    }
}
