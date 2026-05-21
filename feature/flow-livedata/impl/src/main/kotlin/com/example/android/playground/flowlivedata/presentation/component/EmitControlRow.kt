package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
internal fun EmitControlRow(
    isEmitting: Boolean,
    onToggleEmitting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        if (isEmitting) {
            OutlinedButton(
                onClick = onToggleEmitting,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(text = "Stop Emitting")
            }
        } else {
            Button(
                onClick = onToggleEmitting,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(text = "Start Emitting")
            }
        }
    }
}

@DualThemePreview
@Composable
private fun EmitControlRowIdlePreview() {
    PreviewContainer {
        EmitControlRow(
            isEmitting = false,
            onToggleEmitting = {},
        )
    }
}

@DualThemePreview
@Composable
private fun EmitControlRowEmittingPreview() {
    PreviewContainer {
        EmitControlRow(
            isEmitting = true,
            onToggleEmitting = {},
        )
    }
}
