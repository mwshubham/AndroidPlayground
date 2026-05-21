package com.example.android.playground.tictactoe.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.presentation.component.TicTacToeGameContent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeGameSideEffect
import com.example.android.playground.tictactoe.presentation.viewmodel.TicTacToeGameViewModel

@Composable
fun TicTacToeGameScreen(
    route: TicTacToeGameRoute,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicTacToeGameViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(route) {
        viewModel.initGame(route)
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                TicTacToeGameSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    TrackScreenViewEvent(screenName = "TicTacToeGameScreen")

    TicTacToeGameContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
