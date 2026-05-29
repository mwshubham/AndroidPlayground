package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.cryptosecurity.presentation.state.SecureNetworkDemoState
import org.junit.Rule
import org.junit.Test

class SecureNetworkDemoContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                SecureNetworkDemoContent(
                    state = SecureNetworkDemoState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                SecureNetworkDemoContent(
                    state = SecureNetworkDemoState(isLoading = true),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun withDecryptedMessage() {
        paparazzi.snapshot {
            AppTheme {
                SecureNetworkDemoContent(
                    state =
                        SecureNetworkDemoState(
                            decryptedMessage = "Hello from the secure server!",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
