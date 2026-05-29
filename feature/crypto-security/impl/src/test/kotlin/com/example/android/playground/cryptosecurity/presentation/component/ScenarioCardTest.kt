package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ScenarioCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultCard() {
        paparazzi.snapshot {
            AppTheme {
                ScenarioCard(
                    icon = Icons.Default.Lock,
                    title = "Store API Token",
                    description = "Encrypt and store an API token securely in the Keystore",
                    onTryDemo = {},
                )
            }
        }
    }
}
