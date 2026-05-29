package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.presentation.state.AidlState
import org.junit.Rule
import org.junit.Test

class AidlContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun disconnectedState() {
        paparazzi.snapshot {
            AppTheme {
                AidlContent(
                    state = AidlState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun connectedState() {
        paparazzi.snapshot {
            AppTheme {
                AidlContent(
                    state =
                        AidlState(
                            isConnected = true,
                            currentPackage = "com.example.app",
                            targetPackage = "com.example.other",
                            inputText = "Hello from AIDL",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
