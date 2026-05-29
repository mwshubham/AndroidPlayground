package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class EmitControlRowTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun notEmitting() {
        paparazzi.snapshot {
            AppTheme {
                EmitControlRow(isEmitting = false, onToggleEmitting = {})
            }
        }
    }

    @Test
    fun emitting() {
        paparazzi.snapshot {
            AppTheme {
                EmitControlRow(isEmitting = true, onToggleEmitting = {})
            }
        }
    }
}
