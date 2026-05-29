package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import org.junit.Rule
import org.junit.Test

class StatusChipTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun pendingStatus() {
        paparazzi.snapshot {
            AppTheme {
                StatusChip(status = UploadStatus.PENDING)
            }
        }
    }

    @Test
    fun inProgressStatus() {
        paparazzi.snapshot {
            AppTheme {
                StatusChip(status = UploadStatus.IN_PROGRESS)
            }
        }
    }

    @Test
    fun successStatus() {
        paparazzi.snapshot {
            AppTheme {
                StatusChip(status = UploadStatus.SUCCESS)
            }
        }
    }

    @Test
    fun failedStatus() {
        paparazzi.snapshot {
            AppTheme {
                StatusChip(status = UploadStatus.FAILED)
            }
        }
    }
}
