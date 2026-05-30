package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import com.example.android.playground.tictactoe.domain.strategy.AiStrategy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAiMoveUseCaseTest {
    private val aiStrategy: AiStrategy = mockk()
    private lateinit var useCase: GetAiMoveUseCase

    private val aiPlayer = Player(name = "AI", symbol = Symbol.O)

    @Before
    fun setUp() {
        useCase = GetAiMoveUseCase(aiStrategy)
    }

    @Test
    fun invokeDelegatesToAiStrategyAndReturnsMove() {
        val board = GameBoard()
        every { aiStrategy.calculateMove(any(), any()) } returns (1 to 1)

        val move = useCase(board, aiPlayer)

        assertEquals(1 to 1, move)
    }

    @Test
    fun invokePassesBoardAndAiPlayerToStrategy() {
        val board = GameBoard()
        every { aiStrategy.calculateMove(any(), any()) } returns (0 to 0)

        useCase(board, aiPlayer)

        verify(exactly = 1) { aiStrategy.calculateMove(board, aiPlayer) }
    }
}
