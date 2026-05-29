package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class CurrentValueCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withValue() {
        paparazzi.snapshot {
            AppTheme {
                CurrentValueCard(
                    label = "Current Value",
                    value = 42,
                    subtitle = "StateFlow latest emission",
                )
            }
        }
    }

    @Test
    fun zeroValue() {
        paparazzi.snapshot {
            AppTheme {
                CurrentValueCard(
                    label = "Current Value",
                    value = 0,
                    subtitle = "No emissions yet",
                )
            }
        }
    }
}
