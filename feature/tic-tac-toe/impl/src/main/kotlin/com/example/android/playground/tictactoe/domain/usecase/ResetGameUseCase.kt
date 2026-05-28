package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame
import javax.inject.Inject

class ResetGameUseCase
    @Inject
    constructor() {
        operator fun invoke(game: TicTacToeGame): TicTacToeGame =
            game.copy(
                board = GameBoard(),
                currentPlayer = game.playerX,
                status = GameStatus.InProgress,
                moveCount = 0,
            )
    }
