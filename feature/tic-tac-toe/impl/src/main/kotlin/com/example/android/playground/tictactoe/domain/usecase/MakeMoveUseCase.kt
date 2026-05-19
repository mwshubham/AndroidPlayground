package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame
import javax.inject.Inject

private const val BOARD_SIZE = 3

/**
 * Places a move and evaluates the resulting game state.
 *
 * LLD showcase: encapsulates the win/draw detection state-machine transition.
 * State machine: InProgress → Won | Draw.
 */
class MakeMoveUseCase
    @Inject
    constructor() {
        operator fun invoke(
            game: TicTacToeGame,
            row: Int,
            col: Int,
        ): TicTacToeGame {
            if (game.status != GameStatus.InProgress || !game.board.isEmpty(row, col)) return game

            val newBoard = game.board.place(row, col, game.currentPlayer)
            val newMoveCount = game.moveCount + 1
            val status = evaluateStatus(newBoard, game.currentPlayer, newMoveCount)
            val nextPlayer = if (game.currentPlayer == game.playerX) game.playerO else game.playerX

            return game.copy(
                board = newBoard,
                currentPlayer = if (status == GameStatus.InProgress) nextPlayer else game.currentPlayer,
                status = status,
                moveCount = newMoveCount,
            )
        }

        private fun evaluateStatus(
            board: GameBoard,
            lastPlayer: Player,
            moveCount: Int,
        ): GameStatus {
            val winningLine = findWinningLine(board, lastPlayer)
            return when {
                winningLine != null -> GameStatus.Won(winner = lastPlayer, winningCells = winningLine)
                moveCount == BOARD_SIZE * BOARD_SIZE -> GameStatus.Draw
                else -> GameStatus.InProgress
            }
        }

        private fun findWinningLine(
            board: GameBoard,
            player: Player,
        ): List<Pair<Int, Int>>? {
            val size = BOARD_SIZE
            val lines =
                buildList {
                    for (i in 0 until size) {
                        add((0 until size).map { c -> Pair(i, c) })
                        add((0 until size).map { r -> Pair(r, i) })
                    }
                    add((0 until size).map { i -> Pair(i, i) })
                    add((0 until size).map { i -> Pair(i, size - 1 - i) })
                }
            return lines.firstOrNull { line ->
                line.all { (r, c) ->
                    board.cells[r][c].let { it is Cell.Occupied && it.player == player }
                }
            }
        }
    }
