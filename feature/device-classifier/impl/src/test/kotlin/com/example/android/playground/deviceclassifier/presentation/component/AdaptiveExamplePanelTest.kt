package com.example.android.playground.deviceclassifier.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import org.junit.Rule
import org.junit.Test

class AdaptiveExamplePanelTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun lowTier() {
        paparazzi.snapshot {
            AppTheme {
                AdaptiveExamplePanel(tier = DeviceTier.LOW)
            }
        }
    }

    @Test
    fun highTier() {
        paparazzi.snapshot {
            AppTheme {
                AdaptiveExamplePanel(tier = DeviceTier.HIGH)
            }
        }
    }
}
