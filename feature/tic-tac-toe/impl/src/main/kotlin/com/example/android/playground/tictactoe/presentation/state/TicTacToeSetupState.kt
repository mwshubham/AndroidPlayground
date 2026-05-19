package com.example.android.playground.tictactoe.presentation.state

import com.example.android.playground.tictactoe.domain.model.GameMode

data class TicTacToeSetupState(
    val selectedMode: GameMode = GameMode.PlayerVsAi,
    val player1Name: String = "",
    val player2Name: String = "",
    val gamesPlayed: Int = 0,
    val isStartEnabled: Boolean = false,
)
