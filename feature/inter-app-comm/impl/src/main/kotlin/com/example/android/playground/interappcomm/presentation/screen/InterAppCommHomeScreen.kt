package com.example.android.playground.interappcomm.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.presentation.component.InterAppCommHomeContent
import com.example.android.playground.interappcomm.presentation.sideeffect.InterAppCommHomeSideEffect
import com.example.android.playground.interappcomm.presentation.viewmodel.InterAppCommHomeViewModel

@Composable
fun InterAppCommHomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToChannel: (IpcMethod) -> Unit = {},
    viewModel: InterAppCommHomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = "InterAppCommHomeScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                InterAppCommHomeSideEffect.NavigateBack -> onNavigateBack()
                is InterAppCommHomeSideEffect.NavigateToChannel -> onNavigateToChannel(effect.method)
            }
        }
    }

    InterAppCommHomeContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
