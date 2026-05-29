package com.example.android.playground.cryptosecurity.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class HowItWorksCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withSteps() {
        paparazzi.snapshot {
            AppTheme {
                HowItWorksCard(
                    steps =
                        listOf(
                            "Generate key in Keystore",
                            "Encrypt data with AES-GCM",
                            "Store ciphertext + IV",
                        ),
                )
            }
        }
    }

    @Test
    fun singleStep() {
        paparazzi.snapshot {
            AppTheme {
                HowItWorksCard(
                    steps = listOf("Certificate pinning prevents MITM attacks"),
                )
            }
        }
    }
}
