package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.presentation.state.BroadcastState
import org.junit.Rule
import org.junit.Test

class BroadcastContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                BroadcastContent(
                    state = BroadcastState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun withInput() {
        paparazzi.snapshot {
            AppTheme {
                BroadcastContent(
                    state =
                        BroadcastState(
                            currentPackage = "com.example.app",
                            inputText = "Test broadcast message",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
