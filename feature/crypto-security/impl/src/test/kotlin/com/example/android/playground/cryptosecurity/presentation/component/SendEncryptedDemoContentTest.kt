package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.cryptosecurity.presentation.state.SendEncryptedDemoState
import org.junit.Rule
import org.junit.Test

class SendEncryptedDemoContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                SendEncryptedDemoContent(
                    state = SendEncryptedDemoState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun encryptingState() {
        paparazzi.snapshot {
            AppTheme {
                SendEncryptedDemoContent(
                    state = SendEncryptedDemoState(isEncrypting = true),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
