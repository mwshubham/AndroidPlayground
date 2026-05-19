package com.example.android.playground.tictactoe.domain.strategy

import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.GameBoard
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import javax.inject.Inject

private const val MINIMAX_WIN_SCORE = 10

/**
 * Minimax-based AI strategy that plays optimally — it never loses.
 *
 * ## How Minimax works
 *
 * Minimax is a recursive algorithm that models the game as a tree of all possible future states.
 * Each node in the tree is a board position; each edge is a legal move.
 *
 * ### Two players, opposite goals
 * - The **maximising player** is the AI: it wants the highest possible score.
 * - The **minimising player** is the opponent: it wants the lowest possible score.
 *
 * At every level the algorithm alternates between these two roles, assuming both sides always
 * play perfectly. This means the AI picks the move that gives the best outcome *even against a
 * perfect opponent*.
 *
 * ### Scoring terminal states
 * When the algorithm reaches a game-over position it returns a numeric score:
 * - **AI wins** → `+MINIMAX_WIN_SCORE - depth`  (prefer faster wins)
 * - **Opponent wins** → `depth - MINIMAX_WIN_SCORE` (prefer slower losses)
 * - **Draw** → `0`
 *
 * Subtracting / adding `depth` biases the algorithm towards winning quickly and losing slowly,
 * which produces more natural-looking play.
 *
 * ### Propagating scores back up the tree
 * ```
 * Root (AI to move — maximise)
 *   ├─ Move A → score via minimise subtree = +7
 *   ├─ Move B → score via minimise subtree = -3
 *   └─ Move C → score via minimise subtree = +9  ← AI picks this
 * ```
 * At each minimising level the opponent would pick the branch with the smallest score,
 * so the algorithm returns `minOf(...)`. At each maximising level the AI picks `maxOf(...)`.
 *
 * ### Complexity
 * For a 3 × 3 board there are at most 9! = 362 880 leaf positions, so the full tree is
 * evaluated in milliseconds. No pruning (alpha-beta) is needed at this scale.
 *
 * ---
 * **LLD showcase — Strategy pattern**: this class is the concrete strategy bound via Hilt.
 * Swapping the binding to [RandomAiStrategy] requires zero call-site changes.
 */
class MinimaxAiStrategy
    @Inject
    constructor() : AiStrategy {
        /**
         * Evaluates every legal move on [board] via [minimax] and returns the (row, col) pair
         * with the highest resulting score. This is always called as the maximising player's
         * first real move, so [minimax] is invoked with `isMaximising = false` (the opponent's
         * turn follows immediately after each candidate move is placed).
         *
         * @param board the current board before the AI moves
         * @param aiPlayer the player object representing the AI
         * @return the best (row, col) move
         */
        override fun calculateMove(
            board: GameBoard,
            aiPlayer: Player,
        ): Pair<Int, Int> {
            val opponentSymbol = if (aiPlayer.symbol == Symbol.X) Symbol.O else Symbol.X
            var bestScore = Int.MIN_VALUE
            var bestMove = board.emptyCells().first()

            for ((row, col) in board.emptyCells()) {
                val newBoard = board.place(row, col, aiPlayer)
                val score =
                    minimax(
                        board = newBoard,
                        aiSymbol = aiPlayer.symbol,
                        opponentSymbol = opponentSymbol,
                        isMaximising = false,
                        depth = 0,
                    )
                if (score > bestScore) {
                    bestScore = score
                    bestMove = Pair(row, col)
                }
            }
            return bestMove
        }

        /**
         * Core recursive minimax function.
         *
         * **Base cases** — terminal states are detected first and return immediately without
         * recursing:
         * - AI has a complete line → `+MINIMAX_WIN_SCORE - depth`
         * - Opponent has a complete line → `depth - MINIMAX_WIN_SCORE`
         * - No empty cells left (draw) → `0`
         *
         * **Recursive cases**:
         * - `isMaximising = true` (AI's turn): tries every empty cell, recurses with
         *   `isMaximising = false`, returns the maximum child score.
         * - `isMaximising = false` (opponent's turn): tries every empty cell, recurses with
         *   `isMaximising = true`, returns the minimum child score.
         *
         * @param board the board state at this node of the game tree
         * @param aiSymbol the [Symbol] assigned to the AI
         * @param opponentSymbol the [Symbol] assigned to the human opponent
         * @param isMaximising `true` when it is the AI's turn to move
         * @param depth how many half-moves deep this node is; used to prefer shorter wins
         * @return the minimax score for this board state
         */
        private fun minimax(
            board: GameBoard,
            aiSymbol: Symbol,
            opponentSymbol: Symbol,
            isMaximising: Boolean,
            depth: Int,
        ): Int {
            val winner = checkWinner(board)
            val terminalScore =
                when {
                    winner == aiSymbol -> MINIMAX_WIN_SCORE - depth
                    winner == opponentSymbol -> depth - MINIMAX_WIN_SCORE
                    board.emptyCells().isEmpty() -> 0
                    else -> null
                }
            return terminalScore ?: if (isMaximising) {
                board.emptyCells().fold(Int.MIN_VALUE) { best, (row, col) ->
                    val aiPlayer = Player(name = "", symbol = aiSymbol)
                    val next = board.place(row, col, aiPlayer)
                    maxOf(
                        a = best,
                        b =
                            minimax(
                                board = next,
                                aiSymbol = aiSymbol,
                                opponentSymbol = opponentSymbol,
                                isMaximising = false,
                                depth = depth + 1,
                            ),
                    )
                }
            } else {
                board.emptyCells().fold(Int.MAX_VALUE) { best, (row, col) ->
                    val oppPlayer = Player(name = "", symbol = opponentSymbol)
                    val next = board.place(row, col, oppPlayer)
                    minOf(
                        a = best,
                        b =
                            minimax(
                                board = next,
                                aiSymbol = aiSymbol,
                                opponentSymbol = opponentSymbol,
                                isMaximising = true,
                                depth = depth + 1,
                            ),
                    )
                }
            }
        }

        /**
         * Scans all rows, columns, and diagonals of [board] and returns the [Symbol] that
         * occupies an entire line, or `null` if no winner exists yet.
         *
         * A line is "won" when every cell is [Cell.Occupied] by the same symbol.
         */
        private fun checkWinner(board: GameBoard): Symbol? {
            val size = board.cells.size
            val lines =
                buildList {
                    for (i in 0 until size) {
                        add(board.cells[i])
                        add(board.cells.map { it[i] })
                    }
                    add((0 until size).map { board.cells[it][it] })
                    add((0 until size).map { board.cells[it][size - 1 - it] })
                }
            return lines
                .firstOrNull { line ->
                    val symbols = line.filterIsInstance<Cell.Occupied>().map { it.player.symbol }
                    symbols.size == size && symbols.toSet().size == 1
                }?.filterIsInstance<Cell.Occupied>()
                ?.firstOrNull()
                ?.player
                ?.symbol
        }
    }
