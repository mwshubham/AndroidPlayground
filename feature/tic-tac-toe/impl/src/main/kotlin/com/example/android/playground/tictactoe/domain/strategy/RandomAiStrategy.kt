package com.example.android.playground.tictactoe.domain.strategy

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import javax.inject.Inject

class RandomAiStrategy
    @Inject
    constructor() : AiStrategy {
        override fun calculateMove(
            board: GameBoard,
            aiPlayer: Player,
        ): Pair<Int, Int> {
            val emptyCells = board.emptyCells()
            return emptyCells.random()
        }
    }
