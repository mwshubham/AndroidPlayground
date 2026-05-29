package com.example.android.playground.tictactoe.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Rule
import org.junit.Test

class GameBoardGridTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private val playerX = Player(name = "Alice", symbol = Symbol.X)
    private val playerO = Player(name = "Bob", symbol = Symbol.O)

    @Test
    fun emptyBoard() {
        paparazzi.snapshot {
            AppTheme {
                GameBoardGrid(
                    board = GameBoard(),
                    winningCells = emptyList(),
                    onCellClick = { _, _ -> },
                )
            }
        }
    }

    @Test
    fun midGameBoard() {
        val board =
            GameBoard(
                cells =
                    listOf(
                        listOf(Cell.Occupied(playerX), Cell.Occupied(playerO), Cell.Empty),
                        listOf(Cell.Empty, Cell.Occupied(playerX), Cell.Empty),
                        listOf(Cell.Empty, Cell.Empty, Cell.Empty),
                    ),
            )
        paparazzi.snapshot {
            AppTheme {
                GameBoardGrid(
                    board = board,
                    winningCells = emptyList(),
                    onCellClick = { _, _ -> },
                )
            }
        }
    }

    @Test
    fun boardWithWinningCells() {
        val board =
            GameBoard(
                cells =
                    listOf(
                        listOf(Cell.Occupied(playerX), Cell.Occupied(playerX), Cell.Occupied(playerX)),
                        listOf(Cell.Occupied(playerO), Cell.Occupied(playerO), Cell.Empty),
                        listOf(Cell.Empty, Cell.Empty, Cell.Empty),
                    ),
            )
        paparazzi.snapshot {
            AppTheme {
                GameBoardGrid(
                    board = board,
                    winningCells = listOf(0 to 0, 0 to 1, 0 to 2),
                    onCellClick = { _, _ -> },
                )
            }
        }
    }
}
