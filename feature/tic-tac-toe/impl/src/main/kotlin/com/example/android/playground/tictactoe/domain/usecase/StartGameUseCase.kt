package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame
import javax.inject.Inject

class StartGameUseCase
    @Inject
    constructor() {
        operator fun invoke(
            mode: GameMode,
            player1Name: String,
            player2Name: String,
        ): TicTacToeGame {
            val playerX = Player(name = player1Name, symbol = Symbol.X)
            val playerO = Player(name = player2Name, symbol = Symbol.O)
            return TicTacToeGame(
                playerX = playerX,
                playerO = playerO,
                currentPlayer = playerX,
                mode = mode,
            )
        }
    }
