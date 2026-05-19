package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.GameBoard

@Composable
fun GameBoardGrid(
    board: GameBoard,
    winningCells: List<Pair<Int, Int>>,
    onCellClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
    ) {
        board.cells.forEachIndexed { rowIndex, row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEachIndexed { colIndex, cell ->
                    GameBoardCell(
                        cell = cell,
                        isWinningCell = Pair(rowIndex, colIndex) in winningCells,
                        onClick = { onCellClick(rowIndex, colIndex) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun GameBoardGridEmptyPreview() {
    PreviewContainer {
        GameBoardGrid(
            board = GameBoard(),
            winningCells = emptyList(),
            onCellClick = { _, _ -> },
        )
    }
}

@ComponentPreview
@Composable
private fun GameBoardGridWithMovesPreview() {
    val player =
        com.example.android.playground.tictactoe.domain.model.Player(
            name = "Alice",
            symbol = com.example.android.playground.tictactoe.domain.model.Symbol.X,
        )
    val board =
        GameBoard()
            .place(0, 0, player)
            .place(1, 1, player)
    PreviewContainer {
        GameBoardGrid(
            board = board,
            winningCells = emptyList(),
            onCellClick = { _, _ -> },
        )
    }
}
