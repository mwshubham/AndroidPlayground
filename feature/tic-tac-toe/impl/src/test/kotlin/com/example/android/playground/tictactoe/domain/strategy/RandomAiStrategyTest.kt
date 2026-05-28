package com.example.android.playground.tictactoe.domain.strategy

import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RandomAiStrategyTest {
    private val aiPlayer = Player(name = "AI", symbol = Symbol.O)
    private lateinit var strategy: RandomAiStrategy

    @Before
    fun setUp() {
        strategy = RandomAiStrategy()
    }

    @Test
    fun `calculateMove returns a cell that is currently empty`() {
        val board = GameBoard()

        val (row, col) = strategy.calculateMove(board, aiPlayer)

        assertTrue(board.cells[row][col] is Cell.Empty)
    }

    @Test
    fun `calculateMove returns coordinates within valid board bounds`() {
        val board = GameBoard()

        repeat(20) {
            val (row, col) = strategy.calculateMove(board, aiPlayer)
            assertTrue(row in 0..2)
            assertTrue(col in 0..2)
        }
    }

    @Test
    fun `calculateMove on nearly-full board returns the last empty cell`() {
        val x = Player("X", Symbol.X)
        val o = Player("O", Symbol.O)
        // Fill all cells except (2, 2)
        val board =
            GameBoard()
                .place(0, 0, x)
                .place(0, 1, o)
                .place(0, 2, x)
                .place(1, 0, o)
                .place(1, 1, x)
                .place(1, 2, o)
                .place(2, 0, x)
                .place(2, 1, o)

        val (row, col) = strategy.calculateMove(board, aiPlayer)

        assertTrue(row == 2 && col == 2)
    }
}
