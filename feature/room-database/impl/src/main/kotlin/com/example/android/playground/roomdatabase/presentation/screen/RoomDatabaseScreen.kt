package com.example.android.playground.roomdatabase.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.roomdatabase.presentation.component.RoomDatabaseContent
import com.example.android.playground.roomdatabase.presentation.sideeffect.RoomDatabaseSideEffect
import com.example.android.playground.roomdatabase.presentation.viewmodel.RoomDatabaseViewModel

@Composable
fun RoomDatabaseScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: RoomDatabaseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenViewEvent(screenName = "RoomDatabaseScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is RoomDatabaseSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    RoomDatabaseContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
