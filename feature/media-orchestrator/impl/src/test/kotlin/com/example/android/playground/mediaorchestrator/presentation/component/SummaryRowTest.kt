package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.mediaorchestrator.presentation.state.MediaOrchestratorState
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
import org.junit.Rule
import org.junit.Test

class SummaryRowTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                SummaryRow(state = MediaOrchestratorState())
            }
        }
    }

    @Test
    fun withCounts() {
        paparazzi.snapshot {
            AppTheme {
                SummaryRow(
                    state =
                        MediaOrchestratorState(
                            workerStatus = WorkerStatus.RUNNING,
                            totalCount = 10,
                            successCount = 4,
                            failedCount = 1,
                            pendingCount = 3,
                            inProgressCount = 2,
                            overallProgress = 0.5f,
                        ),
                )
            }
        }
    }

    @Test
    fun doneState() {
        paparazzi.snapshot {
            AppTheme {
                SummaryRow(
                    state =
                        MediaOrchestratorState(
                            workerStatus = WorkerStatus.DONE,
                            totalCount = 5,
                            successCount = 4,
                            failedCount = 1,
                            pendingCount = 0,
                            inProgressCount = 0,
                            overallProgress = 1f,
                        ),
                )
            }
        }
    }
}
