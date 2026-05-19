package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.Cell
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol

@Composable
fun GameBoardCell(
    cell: Cell,
    isWinningCell: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        if (isWinningCell) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .aspectRatio(1f)
                .border(width = 2.dp, color = borderColor, shape = MaterialTheme.shapes.medium)
                .clickable(enabled = cell is Cell.Empty, onClick = onClick)
                .padding(4.dp)
                .semantics {
                    contentDescription =
                        when (cell) {
                            Cell.Empty -> "Empty cell"
                            is Cell.Occupied -> "${cell.player.symbol.name} cell"
                        }
                },
    ) {
        AnimatedContent(
            targetState = cell,
            transitionSpec = {
                scaleIn(animationSpec = tween(durationMillis = 200)) togetherWith
                    scaleOut(animationSpec = tween(durationMillis = 200))
            },
            label = "cell_anim",
        ) { target ->
            when (target) {
                Cell.Empty -> Unit
                is Cell.Occupied -> {
                    val (symbol, color) =
                        when (target.player.symbol) {
                            Symbol.X -> "✕" to MaterialTheme.colorScheme.primary
                            Symbol.O -> "○" to MaterialTheme.colorScheme.secondary
                        }
                    Text(
                        text = symbol,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isWinningCell) MaterialTheme.colorScheme.primary else color,
                    )
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun GameBoardCellEmptyPreview() {
    PreviewContainer {
        GameBoardCell(cell = Cell.Empty, isWinningCell = false, onClick = {})
    }
}

@ComponentPreview
@Composable
private fun GameBoardCellXPreview() {
    val player = Player(name = "Alice", symbol = Symbol.X)
    PreviewContainer {
        GameBoardCell(cell = Cell.Occupied(player), isWinningCell = false, onClick = {})
    }
}

@ComponentPreview
@Composable
private fun GameBoardCellOWinningPreview() {
    val player = Player(name = "Bob", symbol = Symbol.O)
    PreviewContainer {
        GameBoardCell(cell = Cell.Occupied(player), isWinningCell = true, onClick = {})
    }
}
