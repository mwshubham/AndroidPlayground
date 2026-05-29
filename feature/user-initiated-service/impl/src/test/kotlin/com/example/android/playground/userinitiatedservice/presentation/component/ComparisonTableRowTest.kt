package com.example.android.playground.userinitiatedservice.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ComparisonTableRowTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultRow() {
        paparazzi.snapshot {
            AppTheme {
                ComparisonTableRow(
                    aspect = "API Level",
                    uij = "API 34+",
                    wmExpedited = "API 14+",
                    regularFgs = "API 14+",
                )
            }
        }
    }
}
