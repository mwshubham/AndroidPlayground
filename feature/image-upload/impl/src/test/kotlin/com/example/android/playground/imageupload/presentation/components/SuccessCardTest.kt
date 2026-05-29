package com.example.android.playground.imageupload.presentation.components

import androidx.compose.foundation.layout.Row
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.imageupload.presentation.ImageUploadState
import org.junit.Rule
import org.junit.Test

class SuccessCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withSuccessCount() {
        paparazzi.snapshot {
            AppTheme {
                Row {
                    SuccessCard(state = ImageUploadState(successCount = 42))
                }
            }
        }
    }
}
