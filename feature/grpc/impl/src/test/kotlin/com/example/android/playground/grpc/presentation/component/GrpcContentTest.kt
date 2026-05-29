package com.example.android.playground.grpc.presentation.component

import androidx.compose.foundation.lazy.rememberLazyListState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.grpc.domain.model.MessageRole
import com.example.android.playground.grpc.presentation.state.ElizaMessageUiModel
import com.example.android.playground.grpc.presentation.state.GrpcState
import org.junit.Rule
import org.junit.Test

class GrpcContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                GrpcContent(
                    state = GrpcState(),
                    onIntent = {},
                    onNavigateBack = {},
                    listState = rememberLazyListState(),
                )
            }
        }
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                GrpcContent(
                    state = GrpcState(isLoading = true, inputText = "Tell me about yourself"),
                    onIntent = {},
                    onNavigateBack = {},
                    listState = rememberLazyListState(),
                )
            }
        }
    }

    @Test
    fun withMessages() {
        paparazzi.snapshot {
            AppTheme {
                GrpcContent(
                    state =
                        GrpcState(
                            messages =
                                listOf(
                                    ElizaMessageUiModel(text = "Hello! How can I help you today?", role = MessageRole.ELIZA),
                                    ElizaMessageUiModel(text = "I feel anxious about my work.", role = MessageRole.USER),
                                    ElizaMessageUiModel(text = "Why do you feel anxious about your work?", role = MessageRole.ELIZA),
                                ),
                            inputText = "",
                            isLoading = false,
                        ),
                    onIntent = {},
                    onNavigateBack = {},
                    listState = rememberLazyListState(),
                )
            }
        }
    }
}
