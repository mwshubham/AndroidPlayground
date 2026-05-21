package com.example.android.playground.tictactoe.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.presentation.component.TicTacToeSetupContent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeSetupSideEffect
import com.example.android.playground.tictactoe.presentation.viewmodel.TicTacToeSetupViewModel

@Composable
fun TicTacToeSetupScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGame: (TicTacToeGameRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicTacToeSetupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TicTacToeSetupSideEffect.NavigateToGame -> onNavigateToGame(effect.route)
                TicTacToeSetupSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    TrackScreenViewEvent(screenName = "TicTacToeSetupScreen")

    TicTacToeSetupContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
