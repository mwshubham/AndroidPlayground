package com.example.android.playground.feed.presentation

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.state.FeedState
import org.junit.Rule
import org.junit.Test

class FeedScreenContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                FeedScreenContent(
                    state = FeedState(isLoading = true),
                    snackbarHostState = SnackbarHostState(),
                    onNavigateBack = {},
                    onTopicClick = {},
                    onRefresh = {},
                )
            }
        }
    }

    @Test
    fun withTopics() {
        paparazzi.snapshot {
            AppTheme {
                FeedScreenContent(
                    state =
                        FeedState(
                            topics =
                                listOf(
                                    Topic(
                                        id = TopicId.NoteApp,
                                        titleRes = android.R.string.ok,
                                        descriptionRes = android.R.string.cancel,
                                    ),
                                    Topic(
                                        id = TopicId.LoginScreen,
                                        titleRes = android.R.string.ok,
                                        descriptionRes = android.R.string.cancel,
                                    ),
                                ),
                        ),
                    snackbarHostState = SnackbarHostState(),
                    onNavigateBack = {},
                    onTopicClick = {},
                    onRefresh = {},
                )
            }
        }
    }

    @Test
    fun errorState() {
        paparazzi.snapshot {
            AppTheme {
                FeedScreenContent(
                    state = FeedState(error = "Failed to load topics"),
                    snackbarHostState = SnackbarHostState(),
                    onNavigateBack = {},
                    onTopicClick = {},
                    onRefresh = {},
                )
            }
        }
    }
}
