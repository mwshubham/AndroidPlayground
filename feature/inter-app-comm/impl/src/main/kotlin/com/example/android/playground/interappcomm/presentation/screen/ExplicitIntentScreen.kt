package com.example.android.playground.interappcomm.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.interappcomm.presentation.component.ExplicitIntentContent
import com.example.android.playground.interappcomm.presentation.sideeffect.ExplicitIntentSideEffect
import com.example.android.playground.interappcomm.presentation.viewmodel.ExplicitIntentViewModel

@Composable
fun ExplicitIntentScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: ExplicitIntentViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    TrackScreenViewEvent(screenName = "ExplicitIntentScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ExplicitIntentSideEffect.NavigateBack -> onNavigateBack()
                is ExplicitIntentSideEffect.LaunchIntent -> {
                    runCatching { context.startActivity(effect.intent) }
                }
                is ExplicitIntentSideEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    ExplicitIntentContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
