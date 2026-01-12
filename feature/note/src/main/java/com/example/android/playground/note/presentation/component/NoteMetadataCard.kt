package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.common.util.DateFormatter
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Reusable metadata card component for displaying note creation and update timestamps
 */
@Composable
fun NoteMetadataCard(
    modifier: Modifier = Modifier,
    createdAtFormatted: String,
    updatedAtFormatted: String,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Created: $createdAtFormatted",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Updated: $updatedAtFormatted",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteMetadataCardPreview() {
    val createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
    val updatedAt = System.currentTimeMillis() - 3600000 // 1 hour ago

    PreviewContainer {
        NoteMetadataCard(
            createdAtFormatted = DateFormatter.formatTimestamp(createdAt),
            updatedAtFormatted = DateFormatter.formatTimestamp(updatedAt),
        )
    }
}

@ComponentPreview
@Composable
private fun NoteMetadataCardSameTimePreview() {
    val now = System.currentTimeMillis()

    PreviewContainer {
        NoteMetadataCard(
            createdAtFormatted = DateFormatter.formatTimestamp(now),
            updatedAtFormatted = DateFormatter.formatTimestamp(now),
        )
    }
}

@ComponentPreview
@Composable
private fun NoteMetadataCardDarkPreview() {
    val createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
    val updatedAt = System.currentTimeMillis() - 7200000 // 2 hours ago

    PreviewContainer(darkTheme = true) {
        NoteMetadataCard(
            createdAtFormatted = DateFormatter.formatTimestamp(createdAt),
            updatedAtFormatted = DateFormatter.formatTimestamp(updatedAt),
        )
    }
}
