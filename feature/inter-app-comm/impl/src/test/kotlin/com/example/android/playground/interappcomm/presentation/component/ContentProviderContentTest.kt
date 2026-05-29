package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.presentation.state.ContentProviderState
import org.junit.Rule
import org.junit.Test

class ContentProviderContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                ContentProviderContent(
                    state = ContentProviderState(),
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
                ContentProviderContent(
                    state =
                        ContentProviderState(
                            currentPackage = "com.example.app",
                            inputText = "Data to share via ContentProvider",
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
