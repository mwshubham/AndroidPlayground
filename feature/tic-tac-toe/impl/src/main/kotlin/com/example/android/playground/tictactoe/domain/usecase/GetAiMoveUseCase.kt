package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.strategy.AiStrategy
import javax.inject.Inject

/**
 * Delegates to the injected [AiStrategy] to calculate the AI's next move.
 *
 * LLD showcase: the use case is decoupled from the concrete strategy implementation.
 * Changing the AI difficulty requires only changing the Hilt binding — no use case change.
 */
class GetAiMoveUseCase
    @Inject
    constructor(
        private val aiStrategy: AiStrategy,
    ) {
        operator fun invoke(
            board: GameBoard,
            aiPlayer: Player,
        ): Pair<Int, Int> = aiStrategy.calculateMove(board, aiPlayer)
    }
