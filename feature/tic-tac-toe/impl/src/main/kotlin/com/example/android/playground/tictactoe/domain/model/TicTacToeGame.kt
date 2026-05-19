package com.example.android.playground.tictactoe.domain.model

data class TicTacToeGame(
    val board: GameBoard = GameBoard(),
    val playerX: Player,
    val playerO: Player,
    val currentPlayer: Player,
    val status: GameStatus = GameStatus.InProgress,
    val mode: GameMode,
    val moveCount: Int = 0,
)
