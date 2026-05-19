package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import com.example.android.playground.tictactoe.domain.model.TicTacToeGame
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeGameIntent
import com.example.android.playground.tictactoe.presentation.state.TicTacToeGameState

@Composable
internal fun TicTacToeGameContent(
    state: TicTacToeGameState,
    onIntent: (TicTacToeGameIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val winningCells = (state.status as? GameStatus.Won)?.winningCells ?: emptyList()

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Tic Tac Toe",
                onNavigationClick = { onIntent(TicTacToeGameIntent.OnNavigateBack) },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 24.dp),
        ) {
            if (state.status == GameStatus.InProgress) {
                PlayerIndicator(
                    currentPlayer = state.currentPlayer,
                    isAiThinking = state.isAiThinking,
                )
            } else {
                GameStatusBanner(status = state.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            GameBoardGrid(
                board = state.board,
                winningCells = winningCells,
                onCellClick = { row, col ->
                    onIntent(TicTacToeGameIntent.OnCellTapped(row, col))
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            ) {
                if (state.status != GameStatus.InProgress) {
                    Button(
                        onClick = { onIntent(TicTacToeGameIntent.OnResetGameTapped) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Play Again")
                    }
                }
                OutlinedButton(
                    onClick = { onIntent(TicTacToeGameIntent.OnNavigateBack) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Back to Setup")
                }
            }
        }
    }
}

// ---- Previews ----

@FullPreview
@Composable
private fun TicTacToeGameContentInProgressPreview() {
    val playerX = Player(name = "Alice", symbol = Symbol.X)
    val playerO = Player(name = "Bob", symbol = Symbol.O)
    val game =
        TicTacToeGame(
            playerX = playerX,
            playerO = playerO,
            currentPlayer = playerX,
            mode = GameMode.PlayerVsPlayer,
        )
    PreviewContainer {
        TicTacToeGameContent(
            state = TicTacToeGameState(game = game),
            onIntent = {},
        )
    }
}

@FullPreview
@Composable
private fun TicTacToeGameContentWonPreview() {
    val playerX = Player(name = "Alice", symbol = Symbol.X)
    val playerO = Player(name = "Bob", symbol = Symbol.O)
    val game =
        TicTacToeGame(
            playerX = playerX,
            playerO = playerO,
            currentPlayer = playerX,
            mode = GameMode.PlayerVsPlayer,
            status = GameStatus.Won(winner = playerX, winningCells = listOf(0 to 0, 0 to 1, 0 to 2)),
        )
    PreviewContainer {
        TicTacToeGameContent(
            state = TicTacToeGameState(game = game),
            onIntent = {},
        )
    }
}
