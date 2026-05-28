package com.example.android.playground.deviceclassifier.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.deviceclassifier.presentation.component.DeviceClassifierContent
import com.example.android.playground.deviceclassifier.presentation.sideeffect.DeviceClassifierSideEffect
import com.example.android.playground.deviceclassifier.presentation.viewmodel.DeviceClassifierViewModel

@Composable
fun DeviceClassifierScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceClassifierViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenViewEvent(screenName = "DeviceClassifierScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DeviceClassifierSideEffect.NavigateBack -> onNavigateBack()
                is DeviceClassifierSideEffect.ShowError -> Unit
            }
        }
    }

    DeviceClassifierContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
