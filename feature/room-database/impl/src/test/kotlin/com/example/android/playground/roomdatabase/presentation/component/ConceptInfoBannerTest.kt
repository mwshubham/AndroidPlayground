package com.example.android.playground.roomdatabase.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ConceptInfoBannerTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultBanner() {
        paparazzi.snapshot {
            AppTheme {
                ConceptInfoBanner(
                    title = "Room Database Concepts",
                    description = "Room provides an abstraction layer over SQLite.",
                )
            }
        }
    }
}
