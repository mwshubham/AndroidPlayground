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
import com.example.android.playground.cryptosecurity.presentation.component.SendEncryptedDemoContent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SendEncryptedDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.viewmodel.SendEncryptedDemoViewModel

@Composable
fun SendEncryptedDemoScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: SendEncryptedDemoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = "SendEncryptedDemoScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SendEncryptedDemoSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                SendEncryptedDemoSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SendEncryptedDemoContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
