package com.example.android.playground.media3player.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.media3player.presentation.intent.Media3PlayerIntent
import com.example.android.playground.media3player.presentation.state.Media3PlayerState

@Composable
internal fun Media3PlayerContent(
    state: Media3PlayerState,
    player: ExoPlayer,
    snackbarHostState: SnackbarHostState,
    onIntent: (Media3PlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { state.videos.size })

    // When the user settles on a new page, load the corresponding video
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onIntent(Media3PlayerIntent.SelectVideo(page))
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        when {
            state.videos.isNotEmpty() -> {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    val isCurrentPage = pageIndex == pagerState.currentPage
                    VideoPage(
                        video = state.videos[pageIndex],
                        isCurrentPage = isCurrentPage,
                        isPlaying = state.isPlaying,
                        isMuted = state.isMuted,
                        currentPositionMs = if (isCurrentPage) state.currentPositionMs else 0L,
                        durationMs = if (isCurrentPage) state.durationMs else 0L,
                        // Only the visible page gets the ExoPlayer instance
                        player = if (isCurrentPage) player else null,
                        onIntent = onIntent,
                    )
                }
            }
            state.isLoading -> {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        // Back button floats above the pager, clear of the DRM badge
        IconButton(
            onClick = { onIntent(Media3PlayerIntent.NavigateBack) },
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Navigate back",
                tint = Color.White,
            )
        }

        // Snackbar for transient error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// ---- Preview ----

@FullPreview
@Composable
private fun Media3PlayerContentLoadingPreview() {
    PreviewContainer {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black),
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
            )
            IconButton(
                onClick = {},
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}
