package com.example.android.playground.feed.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.feed.impl.R
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.components.TopicCard
import com.example.android.playground.feed.util.FeedConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: FeedViewModel = hiltViewModel(),
    onTopicClick: (TopicId) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FeedSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(sideEffect.message)
                }
                is FeedSideEffect.TopicsRefreshed -> {
                    snackbarHostState.showSnackbar("Topics refreshed!")
                }
            }
        }
    }

    FeedScreenContent(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onTopicClick = onTopicClick,
        onRefresh = { viewModel.handleIntent(FeedIntent.RefreshTopics) },
    )

    TrackScreenViewEvent(screenName = FeedConstants.SCREEN_NAME)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenContent(
    modifier: Modifier = Modifier,
    state: FeedState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit = {},
    onTopicClick: (TopicId) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Android Playground",
                onNavigationClick = onNavigateBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                state.isLoading && state.topics.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag("loading_indicator"),
                        )
                    }
                }

                state.error != null && state.topics.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Explore System Design Topics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            items(state.topics) { topic ->
                                TopicCard(
                                    topic = topic,
                                    onClick = { onTopicClick(topic.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DualThemePreview
@Composable
fun FeedScreenPreview() {
    PreviewContainer {
        FeedScreenContent(
            state =
                FeedState(
                    topics = getSampleTopics(),
                    isLoading = false,
                    error = null,
                ),
        )
    }
}

@DualThemePreview
@Composable
fun FeedScreenLoadingPreview() {
    PreviewContainer {
        FeedScreenContent(
            state =
                FeedState(
                    topics = emptyList(),
                    isLoading = true,
                    error = null,
                ),
        )
    }
}

@DualThemePreview
@Composable
fun FeedScreenErrorPreview() {
    PreviewContainer {
        FeedScreenContent(
            state =
                FeedState(
                    topics = emptyList(),
                    isLoading = false,
                    error = "Failed to load topics. Please check your internet connection.",
                ),
        )
    }
}

@DualThemePreview
@Composable
fun FeedScreenRefreshingPreview() {
    PreviewContainer {
        FeedScreenContent(
            state =
                FeedState(
                    isRefreshing = true,
                ),
        )
    }
}

@DualThemePreview
@Composable
fun FeedScreenDarkPreview() {
    PreviewContainer {
        FeedScreenContent(
            state =
                FeedState(
                    topics = getSampleTopics(),
                    isLoading = false,
                    error = null,
                ),
        )
    }
}

private fun getSampleTopics(): List<Topic> =
    listOf(
        Topic(
            id = TopicId.NoteApp,
            titleRes = R.string.topic_title_note_app,
            descriptionRes = R.string.topic_description_note_app,
        ),
        Topic(
            id = TopicId.ImageUploadApp,
            titleRes = R.string.topic_title_image_upload_app,
            descriptionRes = R.string.topic_description_image_upload_app,
        ),
        Topic(
            id = TopicId.ChatApp,
            titleRes = R.string.topic_title_chat_app,
            descriptionRes = R.string.topic_description_chat_app,
        ),
    )
