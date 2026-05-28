package com.example.android.playground.flowlivedata.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.flowlivedata.presentation.component.FlowLiveDataContent
import com.example.android.playground.flowlivedata.presentation.sideeffect.FlowLiveDataSideEffect
import com.example.android.playground.flowlivedata.presentation.viewmodel.FlowLiveDataViewModel

@Composable
fun FlowLiveDataScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlowLiveDataViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenViewEvent(screenName = "FlowLiveDataScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is FlowLiveDataSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    FlowLiveDataContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
