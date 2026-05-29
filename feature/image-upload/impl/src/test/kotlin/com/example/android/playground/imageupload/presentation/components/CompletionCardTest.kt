package com.example.android.playground.imageupload.presentation.components

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class CompletionCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultCard() {
        paparazzi.snapshot {
            AppTheme {
                CompletionCard()
            }
        }
    }
}
