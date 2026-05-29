package com.example.android.playground.annotationprocessing.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingState
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingTab
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class AnnotationProcessingContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun overviewTab() {
        paparazzi.snapshot {
            AppTheme {
                AnnotationProcessingContent(
                    state = AnnotationProcessingState(selectedTab = AnnotationProcessingTab.OVERVIEW),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun pipelineTab() {
        paparazzi.snapshot {
            AppTheme {
                AnnotationProcessingContent(
                    state = AnnotationProcessingState(selectedTab = AnnotationProcessingTab.PIPELINE),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun kspVsKaptTab() {
        paparazzi.snapshot {
            AppTheme {
                AnnotationProcessingContent(
                    state = AnnotationProcessingState(selectedTab = AnnotationProcessingTab.KSP_VS_KAPT),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun liveDemoTab() {
        paparazzi.snapshot {
            AppTheme {
                AnnotationProcessingContent(
                    state = AnnotationProcessingState(selectedTab = AnnotationProcessingTab.LIVE_DEMO),
                    onIntent = {},
                )
            }
        }
    }
}
