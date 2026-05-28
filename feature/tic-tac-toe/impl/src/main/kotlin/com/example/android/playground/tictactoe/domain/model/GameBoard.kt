package com.example.android.playground.tictactoe.domain.model

private const val BOARD_SIZE = 3

data class GameBoard(
    val cells: List<List<Cell>> = List(BOARD_SIZE) { List(BOARD_SIZE) { Cell.Empty } },
) {
    fun isEmpty(
        row: Int,
        col: Int,
    ): Boolean = cells[row][col] is Cell.Empty

    fun place(
        row: Int,
        col: Int,
        player: Player,
    ): GameBoard =
        copy(
            cells =
                cells.mapIndexed { r, rowList ->
                    rowList.mapIndexed { c, cell ->
                        if (r == row && c == col) Cell.Occupied(player) else cell
                    }
                },
        )

    fun emptyCells(): List<Pair<Int, Int>> =
        cells
            .flatMapIndexed { r, rowList ->
                rowList.mapIndexedNotNull { c, cell ->
                    if (cell is Cell.Empty) Pair(r, c) else null
                }
            }
}
