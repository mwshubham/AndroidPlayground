package com.example.android.playground.tictactoe.presentation.sideeffect

sealed interface TicTacToeGameSideEffect {
    data object NavigateBack : TicTacToeGameSideEffect
}
