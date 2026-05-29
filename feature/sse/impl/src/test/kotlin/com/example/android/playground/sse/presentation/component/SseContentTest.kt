package com.example.android.playground.sse.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.sse.presentation.state.SseConnectionStatus
import com.example.android.playground.sse.presentation.state.SseState
import com.example.android.playground.sse.presentation.state.WikipediaChangeUiModel
import org.junit.Rule
import org.junit.Test

class SseContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun disconnectedState() {
        paparazzi.snapshot {
            AppTheme {
                SseContent(
                    state = SseState(connectionStatus = SseConnectionStatus.Disconnected),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }

    @Test
    fun connectingState() {
        paparazzi.snapshot {
            AppTheme {
                SseContent(
                    state = SseState(connectionStatus = SseConnectionStatus.Connecting),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }

    @Test
    fun connectedWithChanges() {
        val changes =
            listOf(
                WikipediaChangeUiModel(
                    title = "Kotlin (programming language)",
                    user = "WikiEditor42",
                    wiki = "enwiki",
                    type = "edit",
                    comment = "Updated JVM section.",
                    formattedTime = "14:32",
                ),
                WikipediaChangeUiModel(
                    title = "Android Studio",
                    user = "TechWriter99",
                    wiki = "enwiki",
                    type = "edit",
                    comment = "Added Narwhal release notes.",
                    formattedTime = "14:30",
                ),
                WikipediaChangeUiModel(
                    title = "Jetpack Compose",
                    user = "DevDocs",
                    wiki = "enwiki",
                    type = "new",
                    comment = "Created stub article.",
                    formattedTime = "14:28",
                ),
            )
        paparazzi.snapshot {
            AppTheme {
                SseContent(
                    state =
                        SseState(
                            connectionStatus = SseConnectionStatus.Connected,
                            changes = changes,
                        ),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }

    @Test
    fun errorState() {
        paparazzi.snapshot {
            AppTheme {
                SseContent(
                    state = SseState(connectionStatus = SseConnectionStatus.Error),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }
}
