package com.example.android.playground.annotationprocessing.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class KspVsKaptRowTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun speedRow() {
        paparazzi.snapshot {
            AppTheme {
                KspVsKaptRow(
                    row =
                        KspVsKaptRowData(
                            aspect = "Speed",
                            ksp = "Up to 2x faster",
                            kapt = "Slower (Java stubs)",
                        ),
                )
            }
        }
    }
}
