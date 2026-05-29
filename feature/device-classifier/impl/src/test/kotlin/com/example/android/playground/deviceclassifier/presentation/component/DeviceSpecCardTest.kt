package com.example.android.playground.deviceclassifier.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import org.junit.Rule
import org.junit.Test

class DeviceSpecCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun highEndSpec() {
        paparazzi.snapshot {
            AppTheme {
                DeviceSpecCard(
                    spec =
                        DeviceSpec(
                            ramMb = 8192L,
                            cpuCores = 8,
                            apiLevel = 34,
                        ),
                )
            }
        }
    }

    @Test
    fun lowEndSpec() {
        paparazzi.snapshot {
            AppTheme {
                DeviceSpecCard(
                    spec =
                        DeviceSpec(
                            ramMb = 2048L,
                            cpuCores = 4,
                            apiLevel = 28,
                        ),
                )
            }
        }
    }
}
