package com.example.android.playground.sse.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.sse.presentation.component.SseContent
import com.example.android.playground.sse.presentation.sideeffect.SseSideEffect
import com.example.android.playground.sse.presentation.viewmodel.SseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SseScreen(
    onNavigateBack: () -> Unit,
    viewModel: SseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { sideEffect ->
            when (sideEffect) {
                is SseSideEffect.ShowError -> {
                    // Errors are surfaced via state connection status
                }
            }
        }
    }

    SseContent(
        state = state,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
    )
}
