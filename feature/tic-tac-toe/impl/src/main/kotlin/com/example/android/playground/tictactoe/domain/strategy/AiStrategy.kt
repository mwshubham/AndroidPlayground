package com.example.android.playground.tictactoe.domain.strategy

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player

fun interface AiStrategy {
    fun calculateMove(
        board: GameBoard,
        aiPlayer: Player,
    ): Pair<Int, Int>
}
