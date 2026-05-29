package com.example.android.playground.interappcomm.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import org.junit.Rule
import org.junit.Test

class IpcMethodCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun explicitIntentCard() {
        paparazzi.snapshot {
            AppTheme {
                IpcMethodCard(
                    channel =
                        IpcChannel(
                            method = IpcMethod.EXPLICIT_INTENT,
                            title = "Explicit Intent",
                            tagline = "Start activities in another app by component name",
                            syncAsync = "Async",
                            dataStyle = "Unstructured",
                            securityLabel = "Package visibility",
                            useCases = listOf("Launch screens", "Share content via ACTION_SEND"),
                        ),
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun aidlCard() {
        paparazzi.snapshot {
            AppTheme {
                IpcMethodCard(
                    channel =
                        IpcChannel(
                            method = IpcMethod.AIDL,
                            title = "AIDL",
                            tagline = "Structured RPC to a remote Service using Binder",
                            syncAsync = "Sync",
                            dataStyle = "Structured",
                            securityLabel = "Permission + signature check",
                            useCases = listOf("Complex data types", "Multi-client scenarios"),
                        ),
                    onClick = {},
                )
            }
        }
    }
}
