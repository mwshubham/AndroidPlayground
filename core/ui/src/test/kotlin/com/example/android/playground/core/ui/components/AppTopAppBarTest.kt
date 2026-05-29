package com.example.android.playground.core.ui.components

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class AppTopAppBarTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withNavigationAndTitle() {
        paparazzi.snapshot {
            AppTheme {
                AppTopAppBar(
                    title = "Screen Title",
                    onNavigationClick = {},
                )
            }
        }
    }

    @Test
    fun defaultNavigation() {
        paparazzi.snapshot {
            AppTheme {
                AppTopAppBar(title = "No Click Handler")
            }
        }
    }
}
