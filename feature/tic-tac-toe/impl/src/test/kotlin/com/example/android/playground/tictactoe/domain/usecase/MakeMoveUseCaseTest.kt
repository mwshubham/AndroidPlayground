package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MakeMoveUseCaseTest {
    private val startGame = StartGameUseCase()
    private lateinit var useCase: MakeMoveUseCase

    @Before
    fun setUp() {
        useCase = MakeMoveUseCase()
    }

    @Test
    fun `valid move places piece and switches current player`() {
        val game = startGame(GameMode.PlayerVsPlayer, "Alice", "Bob")
        val updated = useCase(game, 0, 0)

        assertEquals(1, updated.moveCount)
        assertEquals(game.playerO, updated.currentPlayer) // switched to O after X moves
        assertTrue(updated.board.cells[0][0] is com.example.android.playground.tictactoe.domain.model.Cell.Occupied)
    }

    @Test
    fun `move on occupied cell returns same game unchanged`() {
        val game = startGame(GameMode.PlayerVsPlayer, "Alice", "Bob")
        val afterFirst = useCase(game, 0, 0)
        val afterSecond = useCase(afterFirst, 0, 0) // same cell — should be rejected

        assertEquals(afterFirst, afterSecond)
    }

    @Test
    fun `winning move sets status to Won with correct winner`() {
        // X plays (0,0) (0,1) (0,2) — wins on third move
        var game = startGame(GameMode.PlayerVsPlayer, "Alice", "Bob")
        game = useCase(game, 0, 0) // X
        game = useCase(game, 1, 0) // O
        game = useCase(game, 0, 1) // X
        game = useCase(game, 1, 1) // O
        game = useCase(game, 0, 2) // X wins

        assertTrue(game.status is GameStatus.Won)
        assertEquals("Alice", (game.status as GameStatus.Won).winner.name)
    }

    @Test
    fun `draw is detected when board is full with no winner`() {
        // Force a draw: X O X / X X O / O X O
        var game = startGame(GameMode.PlayerVsPlayer, "Alice", "Bob")
        game = useCase(game, 0, 0) // X
        game = useCase(game, 0, 1) // O
        game = useCase(game, 0, 2) // X
        game = useCase(game, 1, 0) // O
        game = useCase(game, 1, 2) // X
        game = useCase(game, 1, 1) // O
        game = useCase(game, 2, 1) // X
        game = useCase(game, 2, 2) // O
        game = useCase(game, 2, 0) // X — board full, no winner

        assertEquals(GameStatus.Draw, game.status)
    }

    @Test
    fun `move after game is over returns same game unchanged`() {
        var game = startGame(GameMode.PlayerVsPlayer, "Alice", "Bob")
        game = useCase(game, 0, 0)
        game = useCase(game, 1, 0)
        game = useCase(game, 0, 1)
        game = useCase(game, 1, 1)
        game = useCase(game, 0, 2) // X wins

        val afterWin = useCase(game, 2, 2) // move after game over
        assertEquals(game, afterWin)
    }
}
