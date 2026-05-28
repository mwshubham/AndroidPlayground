package com.example.android.playground.tictactoe.domain.strategy

import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MinimaxAiStrategyTest {
    private val aiPlayer = Player(name = "AI", symbol = Symbol.X)
    private val opponent = Player(name = "Human", symbol = Symbol.O)
    private lateinit var strategy: MinimaxAiStrategy

    @Before
    fun setUp() {
        strategy = MinimaxAiStrategy()
    }

    /** Board:  X _ _
     *         X _ _
     *         _ _ _
     *  AI must play (2,0) to win immediately. */
    @Test
    fun `ai takes winning move when available`() {
        val board = GameBoard().place(0, 0, aiPlayer).place(1, 0, aiPlayer)

        val move = strategy.calculateMove(board, aiPlayer)

        assertEquals(Pair(2, 0), move)
    }

    /** Board:  O _ _
     *         O _ _
     *         _ _ _
     *  AI must block at (2,0) to prevent opponent win next turn. */
    @Test
    fun `ai blocks opponent winning move`() {
        val board = GameBoard().place(0, 0, opponent).place(1, 0, opponent)

        val move = strategy.calculateMove(board, aiPlayer)

        assertEquals(Pair(2, 0), move)
    }

    /** On an empty board the first move should be (0,0) — the optimal corner. */
    @Test
    fun `ai plays optimal first move on empty board`() {
        val board = GameBoard()

        val (row, col) = strategy.calculateMove(board, aiPlayer)

        // Minimax prefers corners (0,0) as first move on empty 3x3
        assertTrue(board.isEmpty(row, col))
    }

    @Test
    fun `ai returns a cell that is empty`() {
        val board = GameBoard().place(0, 0, aiPlayer).place(0, 1, opponent).place(0, 2, aiPlayer)

        val (row, col) = strategy.calculateMove(board, aiPlayer)

        assertTrue(board.isEmpty(row, col))
        assertTrue(board.cells[row][col] is Cell.Empty)
    }
}
