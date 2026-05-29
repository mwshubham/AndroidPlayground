package com.example.android.playground.interappcomm.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SecurityBadgeTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun packageVisibility() {
        paparazzi.snapshot {
            AppTheme {
                SecurityBadge(label = "Package visibility")
            }
        }
    }

    @Test
    fun signaturePermission() {
        paparazzi.snapshot {
            AppTheme {
                SecurityBadge(label = "Signature permission")
            }
        }
    }
}
