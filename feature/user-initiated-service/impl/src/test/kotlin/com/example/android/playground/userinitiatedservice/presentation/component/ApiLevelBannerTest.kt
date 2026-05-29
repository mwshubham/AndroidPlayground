package com.example.android.playground.userinitiatedservice.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ApiLevelBannerTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun api34Banner() {
        paparazzi.snapshot {
            AppTheme {
                ApiLevelBanner(isApi34Plus = true)
            }
        }
    }
}
