package com.example.android.playground.tictactoe.presentation.state

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame

data class TicTacToeGameState(
    val game: TicTacToeGame =
        TicTacToeGame(
            playerX = Player(name = "Player 1", symbol = Symbol.X),
            playerO = Player(name = "Player 2", symbol = Symbol.O),
            currentPlayer = Player(name = "Player 1", symbol = Symbol.X),
            mode = GameMode.PlayerVsPlayer,
        ),
    val isAiThinking: Boolean = false,
) {
    val board: GameBoard get() = game.board
    val currentPlayer: Player get() = game.currentPlayer
    val status: GameStatus get() = game.status
}
