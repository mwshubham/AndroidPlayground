package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.presentation.state.ExplicitIntentState
import org.junit.Rule
import org.junit.Test

class ExplicitIntentContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                ExplicitIntentContent(
                    state = ExplicitIntentState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun appInstalled() {
        paparazzi.snapshot {
            AppTheme {
                ExplicitIntentContent(
                    state =
                        ExplicitIntentState(
                            isOtherAppInstalled = true,
                            currentPackage = "com.example.app",
                            targetPackage = "com.example.other",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
