package com.example.android.playground.imageupload.presentation.components

import androidx.compose.foundation.layout.Row
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.imageupload.presentation.ImageUploadState
import org.junit.Rule
import org.junit.Test

class FailureCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withFailureCount() {
        paparazzi.snapshot {
            AppTheme {
                Row {
                    FailureCard(state = ImageUploadState(failureCount = 3))
                }
            }
        }
    }
}
