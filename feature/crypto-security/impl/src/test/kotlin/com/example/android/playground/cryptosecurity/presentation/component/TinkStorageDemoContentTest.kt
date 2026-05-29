package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.cryptosecurity.presentation.state.StorageDemoState
import org.junit.Rule
import org.junit.Test

class TinkStorageDemoContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun idleState() {
        paparazzi.snapshot {
            AppTheme {
                TinkStorageDemoContent(
                    state = StorageDemoState(),
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
                TinkStorageDemoContent(
                    state = StorageDemoState(isSaving = true),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
