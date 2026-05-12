package com.example.android.playground.websocket.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.websocket.presentation.component.WebSocketContent
import com.example.android.playground.websocket.presentation.sideeffect.WebSocketSideEffect
import com.example.android.playground.websocket.presentation.viewmodel.WebSocketViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WebSocketScreen(
    onNavigateBack: () -> Unit,
    viewModel: WebSocketViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { sideEffect ->
            when (sideEffect) {
                is WebSocketSideEffect.ShowError -> {
                    // Errors are surfaced in the UI via state — no snackbar needed for MVP
                }
            }
        }
    }

    WebSocketContent(
        state = state,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
    )
}
