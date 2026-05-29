package com.example.android.playground.media3player.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ElegantSeekBarTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun atStart() {
        paparazzi.snapshot {
            AppTheme {
                ElegantSeekBar(
                    progress = 0f,
                    onSeekFinish = {},
                )
            }
        }
    }

    @Test
    fun midProgress() {
        paparazzi.snapshot {
            AppTheme {
                ElegantSeekBar(
                    progress = 0.5f,
                    onSeekFinish = {},
                )
            }
        }
    }
}
