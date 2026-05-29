package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.cryptosecurity.presentation.state.StorageDemoState
import org.junit.Rule
import org.junit.Test

class KeystoreStorageDemoContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                KeystoreStorageDemoContent(
                    state = StorageDemoState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun withSavedData() {
        paparazzi.snapshot {
            AppTheme {
                KeystoreStorageDemoContent(
                    state =
                        StorageDemoState(
                            inputKey = "api_token",
                            inputValue = "secret-token-value",
                            savedIvHex = "a1b2c3d4e5f6a7b8",
                            savedCiphertextHex = "1234567890abcdef",
                            loadedValue = "secret-token-value",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
