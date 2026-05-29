package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SummaryLabelTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun labelWithValue() {
        paparazzi.snapshot {
            AppTheme {
                SummaryLabel(label = "Total", value = "12")
            }
        }
    }

    @Test
    fun labelWithZeroValue() {
        paparazzi.snapshot {
            AppTheme {
                SummaryLabel(label = "Failed", value = "0")
            }
        }
    }
}
