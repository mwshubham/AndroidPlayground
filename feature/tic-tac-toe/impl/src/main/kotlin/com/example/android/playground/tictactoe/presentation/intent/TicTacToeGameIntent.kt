package com.example.android.playground.tictactoe.presentation.intent

sealed interface TicTacToeGameIntent {
    data class OnCellTapped(
        val row: Int,
        val col: Int,
    ) : TicTacToeGameIntent

    data object OnResetGameTapped : TicTacToeGameIntent

    data object OnNavigateBack : TicTacToeGameIntent
}
