package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.presentation.state.MessengerState
import org.junit.Rule
import org.junit.Test

class MessengerContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun disconnectedState() {
        paparazzi.snapshot {
            AppTheme {
                MessengerContent(
                    state = MessengerState(),
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
                MessengerContent(
                    state =
                        MessengerState(
                            isConnected = true,
                            currentPackage = "com.example.app",
                            inputText = "Hello via Messenger",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
