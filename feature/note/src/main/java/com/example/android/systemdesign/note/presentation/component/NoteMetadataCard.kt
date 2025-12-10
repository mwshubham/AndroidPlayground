package com.example.android.systemdesign.note.presentation.component

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
import com.example.android.systemdesign.core.ui.preview.ComponentPreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Reusable metadata card component for displaying note creation and update timestamps
 */
@Composable
fun NoteMetadataCard(
    modifier: Modifier = Modifier,
    createdAt: Long,
    updatedAt: Long
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Created: ${formatDate(createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Updated: ${formatDate(updatedAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteMetadataCardPreview() {
    PreviewContainer {
        NoteMetadataCard(
            createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
            updatedAt = System.currentTimeMillis() - 3600000   // 1 hour ago
        )
    }
}

@ComponentPreview
@Composable
private fun NoteMetadataCardSameTimePreview() {
    PreviewContainer {
        val now = System.currentTimeMillis()
        NoteMetadataCard(
            createdAt = now,
            updatedAt = now
        )
    }
}

@ComponentPreview
@Composable
private fun NoteMetadataCardDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteMetadataCard(
            createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
            updatedAt = System.currentTimeMillis() - 7200000    // 2 hours ago
        )
    }
}
