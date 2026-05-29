package com.example.android.playground.cryptosecurity.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class CryptoInfoCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultCard() {
        paparazzi.snapshot {
            AppTheme {
                CryptoInfoCard(
                    label = "Android Keystore",
                    value = "Hardware-backed secure storage for cryptographic keys",
                )
            }
        }
    }
}
