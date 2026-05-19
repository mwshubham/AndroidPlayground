package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeSetupIntent
import com.example.android.playground.tictactoe.presentation.state.TicTacToeSetupState
import com.example.android.playground.tictactoe.util.TicTacToeConstants

@Composable
internal fun TicTacToeSetupContent(
    state: TicTacToeSetupState,
    onIntent: (TicTacToeSetupIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Tic Tac Toe",
                onNavigationClick = { onIntent(TicTacToeSetupIntent.OnNavigateBack) },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = "Game Mode",
                style = MaterialTheme.typography.titleMedium,
            )

            GameModePicker(
                selectedMode = state.selectedMode,
                onModeSelected = { onIntent(TicTacToeSetupIntent.OnModeSelected(it)) },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Player Names",
                style = MaterialTheme.typography.titleMedium,
            )

            val isAiMode = state.selectedMode is GameMode.PlayerVsAi

            OutlinedTextField(
                value = state.player1Name,
                onValueChange = { onIntent(TicTacToeSetupIntent.OnPlayer1NameChanged(it)) },
                label = { Text(text = if (isAiMode) "Player (X)" else "Player 1 (X)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.player2Name,
                onValueChange = { onIntent(TicTacToeSetupIntent.OnPlayer2NameChanged(it)) },
                label = { Text(text = if (isAiMode) "AI (O)" else "Player 2 (O)") },
                singleLine = true,
                enabled = !isAiMode,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.gamesPlayed > 0) {
                Text(
                    text = "Games played: ${state.gamesPlayed}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onIntent(TicTacToeSetupIntent.OnStartGameTapped) },
                enabled = state.isStartEnabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Start Game")
            }
        }
    }
}

// ---- Previews ----

@FullPreview
@Composable
private fun TicTacToeSetupContentPreview() {
    PreviewContainer {
        TicTacToeSetupContent(
            state =
                TicTacToeSetupState(
                    selectedMode = GameMode.PlayerVsAi,
                    player1Name = "Alice",
                    player2Name = TicTacToeConstants.AI_PLAYER_NAME,
                    gamesPlayed = 3,
                    isStartEnabled = true,
                ),
            onIntent = {},
        )
    }
}

@FullPreview
@Composable
private fun TicTacToeSetupContentEmptyPreview() {
    PreviewContainer {
        TicTacToeSetupContent(
            state = TicTacToeSetupState(),
            onIntent = {},
        )
    }
}
