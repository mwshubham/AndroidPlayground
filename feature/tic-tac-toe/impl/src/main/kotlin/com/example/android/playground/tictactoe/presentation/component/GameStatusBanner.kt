package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol

@Composable
fun GameStatusBanner(
    status: GameStatus,
    modifier: Modifier = Modifier,
) {
    if (status == GameStatus.InProgress) return

    val (title, subtitle, containerColor) =
        when (status) {
            is GameStatus.Won ->
                Triple(
                    "🎉 ${status.winner.name} wins!",
                    "Congratulations!",
                    MaterialTheme.colorScheme.primaryContainer,
                )
            GameStatus.Draw ->
                Triple(
                    "🤝 It's a draw!",
                    "Well played by both!",
                    MaterialTheme.colorScheme.tertiaryContainer,
                )
            GameStatus.InProgress -> return
        }

    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun GameStatusBannerWonPreview() {
    val winner = Player(name = "Alice", symbol = Symbol.X)
    PreviewContainer {
        GameStatusBanner(status = GameStatus.Won(winner = winner, winningCells = emptyList()))
    }
}

@ComponentPreview
@Composable
private fun GameStatusBannerDrawPreview() {
    PreviewContainer {
        GameStatusBanner(status = GameStatus.Draw)
    }
}
