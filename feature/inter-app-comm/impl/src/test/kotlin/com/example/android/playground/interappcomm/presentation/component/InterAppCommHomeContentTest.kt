package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.presentation.state.InterAppCommHomeState
import org.junit.Rule
import org.junit.Test

class InterAppCommHomeContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                InterAppCommHomeContent(
                    state = InterAppCommHomeState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun withChannels() {
        paparazzi.snapshot {
            AppTheme {
                InterAppCommHomeContent(
                    state =
                        InterAppCommHomeState(
                            currentPackage = "com.example.app",
                            targetPackage = "com.example.other",
                            isOtherAppInstalled = true,
                            ipcChannels =
                                listOf(
                                    IpcChannel(
                                        method = IpcMethod.EXPLICIT_INTENT,
                                        title = "Explicit Intent",
                                        tagline = "Start activities in another app",
                                        syncAsync = "Async",
                                        dataStyle = "Unstructured",
                                        securityLabel = "Package visibility",
                                        useCases = listOf("Launch screens", "Share content"),
                                    ),
                                ),
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
