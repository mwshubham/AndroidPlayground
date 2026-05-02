package com.example.android.playground.media3player.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.media3player.presentation.component.Media3PlayerContent
import com.example.android.playground.media3player.presentation.intent.Media3PlayerIntent
import com.example.android.playground.media3player.presentation.sideeffect.Media3PlayerSideEffect
import com.example.android.playground.media3player.presentation.viewmodel.Media3PlayerViewModel
import com.example.android.playground.media3player.util.Media3PlayerConstants

@Composable
fun Media3PlayerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: Media3PlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = Media3PlayerConstants.SCREEN_NAME)

    // Collect side effects
    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is Media3PlayerSideEffect.NavigateBack -> onNavigateBack()
                is Media3PlayerSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    // Pause player when app is backgrounded; resume when foregrounded
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.handleIntent(Media3PlayerIntent.OnLifecyclePause)
                Lifecycle.Event.ON_RESUME -> viewModel.handleIntent(Media3PlayerIntent.OnLifecycleResume)
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Media3PlayerContent(
        state = state,
        player = viewModel.player,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
