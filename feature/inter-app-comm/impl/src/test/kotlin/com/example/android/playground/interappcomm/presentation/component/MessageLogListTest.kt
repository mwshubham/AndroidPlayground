package com.example.android.playground.interappcomm.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import org.junit.Rule
import org.junit.Test

class MessageLogListTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyList() {
        paparazzi.snapshot {
            AppTheme {
                MessageLogList(messages = emptyList<IpcMessage>())
            }
        }
    }

    @Test
    fun withMessages() {
        paparazzi.snapshot {
            AppTheme {
                MessageLogList(
                    messages =
                        listOf(
                            IpcMessage(
                                content = "Hello from our app!",
                                sender = "com.example.app",
                                method = IpcMethod.BROADCAST,
                                direction = MessageDirection.SENT,
                            ),
                            IpcMessage(
                                content = "Got your broadcast!",
                                sender = "com.example.other",
                                method = IpcMethod.BROADCAST,
                                direction = MessageDirection.RECEIVED,
                            ),
                        ),
                )
            }
        }
    }
}
