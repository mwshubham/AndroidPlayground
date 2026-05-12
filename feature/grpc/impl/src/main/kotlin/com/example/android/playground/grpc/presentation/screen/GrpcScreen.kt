package com.example.android.playground.grpc.presentation.screen

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.grpc.presentation.component.GrpcContent
import com.example.android.playground.grpc.presentation.sideeffect.GrpcSideEffect
import com.example.android.playground.grpc.presentation.viewmodel.GrpcViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GrpcScreen(
    onNavigateBack: () -> Unit,
    viewModel: GrpcViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { sideEffect ->
            when (sideEffect) {
                GrpcSideEffect.ScrollToBottom -> {
                    if (state.messages.isNotEmpty()) {
                        listState.animateScrollToItem(state.messages.lastIndex)
                    }
                }

                is GrpcSideEffect.ShowError -> Unit
            }
        }
    }

    GrpcContent(
        state = state,
        listState = listState,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
    )
}
