package com.example.android.playground.tictactoe.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.tictactoe.domain.model.GameMode

@Composable
fun GameModePicker(
    selectedMode: GameMode,
    onModeSelected: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        FilterChip(
            selected = selectedMode is GameMode.PlayerVsAi,
            onClick = { onModeSelected(GameMode.PlayerVsAi) },
            label = { Text(text = "vs AI") },
            modifier = Modifier.weight(1f),
        )
        FilterChip(
            selected = selectedMode is GameMode.PlayerVsPlayer,
            onClick = { onModeSelected(GameMode.PlayerVsPlayer) },
            label = { Text(text = "vs Player") },
            modifier = Modifier.weight(1f),
        )
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun GameModePickerVsAiPreview() {
    PreviewContainer {
        GameModePicker(
            selectedMode = GameMode.PlayerVsAi,
            onModeSelected = {},
        )
    }
}

@ComponentPreview
@Composable
private fun GameModePickerVsPlayerPreview() {
    PreviewContainer {
        GameModePicker(
            selectedMode = GameMode.PlayerVsPlayer,
            onModeSelected = {},
        )
    }
}
