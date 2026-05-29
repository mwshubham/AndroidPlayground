package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class EmissionLogCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyLog() {
        paparazzi.snapshot {
            AppTheme {
                EmissionLogCard(
                    title = "Collector A",
                    entries = emptyList(),
                    onClear = {},
                )
            }
        }
    }

    @Test
    fun withEntries() {
        paparazzi.snapshot {
            AppTheme {
                EmissionLogCard(
                    title = "Collector A",
                    entries =
                        listOf(
                            "14:32:01 — Emitted: 1",
                            "14:32:02 — Emitted: 2",
                            "14:32:03 — Emitted: 3",
                        ),
                    onClear = {},
                )
            }
        }
    }
}
