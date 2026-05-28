package com.example.android.playground.tictactoe.presentation.intent

import com.example.android.playground.tictactoe.domain.model.GameMode

sealed interface TicTacToeSetupIntent {
    data class OnModeSelected(
        val mode: GameMode,
    ) : TicTacToeSetupIntent

    data class OnPlayer1NameChanged(
        val name: String,
    ) : TicTacToeSetupIntent

    data class OnPlayer2NameChanged(
        val name: String,
    ) : TicTacToeSetupIntent

    data object OnStartGameTapped : TicTacToeSetupIntent

    data object OnNavigateBack : TicTacToeSetupIntent
}
