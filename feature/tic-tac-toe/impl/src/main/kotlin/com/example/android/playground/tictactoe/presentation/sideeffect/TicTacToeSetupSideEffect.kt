package com.example.android.playground.tictactoe.presentation.sideeffect

import com.example.android.playground.tictactoe.api.TicTacToeGameRoute

sealed interface TicTacToeSetupSideEffect {
    data class NavigateToGame(
        val route: TicTacToeGameRoute,
    ) : TicTacToeSetupSideEffect

    data object NavigateBack : TicTacToeSetupSideEffect
}
