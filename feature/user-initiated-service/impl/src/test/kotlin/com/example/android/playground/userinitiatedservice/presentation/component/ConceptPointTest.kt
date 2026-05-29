package com.example.android.playground.userinitiatedservice.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ConceptPointTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultPoint() {
        paparazzi.snapshot {
            AppTheme {
                ConceptPoint(
                    label = "Guaranteed",
                    text = "Guaranteed to complete even if the user leaves the app",
                )
            }
        }
    }
}
