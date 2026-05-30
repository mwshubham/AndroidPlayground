package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ResetGameUseCaseTest {
    private lateinit var useCase: ResetGameUseCase

    private val playerX = Player(name = "Alice", symbol = Symbol.X)
    private val playerO = Player(name = "Bob", symbol = Symbol.O)

    @Before
    fun setUp() {
        useCase = ResetGameUseCase()
    }

    private fun makeGame(
        status: GameStatus = GameStatus.InProgress,
        moveCount: Int = 3,
    ) = TicTacToeGame(
        playerX = playerX,
        playerO = playerO,
        currentPlayer = playerO,
        board = GameBoard(),
        status = status,
        mode = GameMode.PlayerVsPlayer,
        moveCount = moveCount,
    )

    @Test
    fun invokeResetsBoardToEmpty() {
        val reset = useCase(makeGame())

        assertEquals(9, reset.board.emptyCells().size)
    }

    @Test
    fun invokeResetsCurrentPlayerToPlayerX() {
        val reset = useCase(makeGame())

        assertEquals(playerX, reset.currentPlayer)
    }

    @Test
    fun invokeResetsStatusToInProgress() {
        val wonStatus = GameStatus.Won(winner = playerX, winningCells = listOf(0 to 0, 0 to 1, 0 to 2))
        val reset = useCase(makeGame(status = wonStatus))

        assertEquals(GameStatus.InProgress, reset.status)
    }

    @Test
    fun invokeResetsMoveCountToZero() {
        val reset = useCase(makeGame(moveCount = 7))

        assertEquals(0, reset.moveCount)
    }
}
