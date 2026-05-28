package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StartGameUseCaseTest {
    private lateinit var useCase: StartGameUseCase

    @Before
    fun setUp() {
        useCase = StartGameUseCase()
    }

    @Test
    fun `invoke creates game with correct player names and symbols`() {
        val game = useCase(GameMode.PlayerVsPlayer, "Alice", "Bob")

        assertEquals("Alice", game.playerX.name)
        assertEquals(Symbol.X, game.playerX.symbol)
        assertEquals("Bob", game.playerO.name)
        assertEquals(Symbol.O, game.playerO.symbol)
    }

    @Test
    fun `invoke sets current player to playerX`() {
        val game = useCase(GameMode.PlayerVsPlayer, "Alice", "Bob")

        assertEquals(game.playerX, game.currentPlayer)
    }

    @Test
    fun `invoke initializes game with InProgress status and empty board`() {
        val game = useCase(GameMode.PlayerVsPlayer, "Alice", "Bob")

        assertEquals(GameStatus.InProgress, game.status)
        assertEquals(0, game.moveCount)
        assertTrue(game.board.emptyCells().size == 9)
    }

    @Test
    fun `invoke sets the supplied mode`() {
        val game = useCase(GameMode.PlayerVsAi, "Human", "AI")

        assertEquals(GameMode.PlayerVsAi, game.mode)
    }
}
