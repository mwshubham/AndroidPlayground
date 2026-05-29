package com.example.android.playground.deviceclassifier.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import com.example.android.playground.deviceclassifier.presentation.state.DeviceClassifierState
import org.junit.Rule
import org.junit.Test

class DeviceClassifierContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                DeviceClassifierContent(
                    state = DeviceClassifierState(isLoading = true),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun mediumTier() {
        paparazzi.snapshot {
            AppTheme {
                DeviceClassifierContent(
                    state =
                        DeviceClassifierState(
                            effectiveTier = DeviceTier.MEDIUM,
                            simulatedRamMb = 4096L,
                            simulatedCpuCores = 8,
                        ),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun highTier() {
        paparazzi.snapshot {
            AppTheme {
                DeviceClassifierContent(
                    state =
                        DeviceClassifierState(
                            effectiveTier = DeviceTier.HIGH,
                            simulatedRamMb = 8192L,
                            simulatedCpuCores = 12,
                        ),
                    onIntent = {},
                )
            }
        }
    }
}
