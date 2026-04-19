package com.example.android.playground.cryptosecurity.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.cryptosecurity.presentation.component.SecureNetworkDemoContent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SecureNetworkDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.viewmodel.SecureNetworkDemoViewModel

@Composable
fun SecureNetworkDemoScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: SecureNetworkDemoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = "SecureNetworkDemoScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SecureNetworkDemoSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                SecureNetworkDemoSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SecureNetworkDemoContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
