package com.example.android.playground.deviceclassifier.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class DeviceParamSliderTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun ramSlider() {
        paparazzi.snapshot {
            AppTheme {
                DeviceParamSlider(
                    label = "RAM (GB)",
                    valueDisplay = "4 GB",
                    value = 4f,
                    valueRange = 1f..16f,
                    onValueChange = {},
                )
            }
        }
    }
}
