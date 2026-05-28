package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol

@Composable
fun PlayerIndicator(
    currentPlayer: Player,
    isAiThinking: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            val symbolText =
                when (currentPlayer.symbol) {
                    Symbol.X -> "✕"
                    Symbol.O -> "○"
                }
            val symbolColor =
                when (currentPlayer.symbol) {
                    Symbol.X -> MaterialTheme.colorScheme.primary
                    Symbol.O -> MaterialTheme.colorScheme.secondary
                }
            Text(
                text = symbolText,
                color = symbolColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAiThinking) "AI is thinking…" else "${currentPlayer.name}'s turn",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun PlayerIndicatorXPreview() {
    val player = Player(name = "Alice", symbol = Symbol.X)
    PreviewContainer {
        PlayerIndicator(currentPlayer = player, isAiThinking = false)
    }
}

@ComponentPreview
@Composable
private fun PlayerIndicatorAiThinkingPreview() {
    val player = Player(name = "AI", symbol = Symbol.O)
    PreviewContainer {
        PlayerIndicator(currentPlayer = player, isAiThinking = true)
    }
}
