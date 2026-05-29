package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class LateSubscriberCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun waiting() {
        paparazzi.snapshot {
            AppTheme {
                LateSubscriberCard(result = "Waiting...", onSimulate = {})
            }
        }
    }

    @Test
    fun received() {
        paparazzi.snapshot {
            AppTheme {
                LateSubscriberCard(result = "Received: 42", onSimulate = {})
            }
        }
    }
}
