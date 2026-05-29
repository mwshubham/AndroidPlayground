package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
import org.junit.Rule
import org.junit.Test

class WorkerStatusBannerTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleStatus() {
        paparazzi.snapshot {
            AppTheme {
                WorkerStatusBanner(workerStatus = WorkerStatus.IDLE)
            }
        }
    }

    @Test
    fun runningStatus() {
        paparazzi.snapshot {
            AppTheme {
                WorkerStatusBanner(workerStatus = WorkerStatus.RUNNING)
            }
        }
    }

    @Test
    fun doneStatus() {
        paparazzi.snapshot {
            AppTheme {
                WorkerStatusBanner(workerStatus = WorkerStatus.DONE)
            }
        }
    }
}
