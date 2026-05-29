package com.example.android.playground.annotationprocessing.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class PipelineSectionTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultSection() {
        paparazzi.snapshot {
            AppTheme {
                PipelineSection()
            }
        }
    }
}
