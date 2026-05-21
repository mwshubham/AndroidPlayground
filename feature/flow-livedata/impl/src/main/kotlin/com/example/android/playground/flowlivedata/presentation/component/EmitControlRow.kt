package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
internal fun EmitControlRow(
    isEmitting: Boolean,
    collectorCount: Int,
    showCollectorControl: Boolean,
    onToggleEmitting: () -> Unit,
    onAddCollector: () -> Unit,
    onRemoveCollector: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isEmitting) {
                    OutlinedButton(
                        onClick = onToggleEmitting,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Stop Emitting")
                    }
                } else {
                    Button(
                        onClick = onToggleEmitting,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Start Emitting")
                    }
                }
            }
            if (showCollectorControl) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Collectors:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(
                        onClick = onRemoveCollector,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Remove collector")
                    }
                    Text(
                        text = "$collectorCount",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(
                        onClick = onAddCollector,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add collector")
                    }
                }
                Text(
                    text = "SharedFlow fans-out to $collectorCount collector${if (collectorCount > 1) "s" else ""} simultaneously.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
            collectorCount = 1,
            showCollectorControl = false,
            onToggleEmitting = {},
            onAddCollector = {},
            onRemoveCollector = {},
        )
    }
}

@DualThemePreview
@Composable
private fun EmitControlRowEmittingWithCollectorsPreview() {
    PreviewContainer {
        EmitControlRow(
            isEmitting = true,
            collectorCount = 2,
            showCollectorControl = true,
            onToggleEmitting = {},
            onAddCollector = {},
            onRemoveCollector = {},
        )
    }
}
