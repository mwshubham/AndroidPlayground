package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.android.playground.common.AppConstants
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.presentation.mapper.NoteUiMapper
import com.example.android.playground.note.presentation.model.NoteListUiModel

/**
 * Reusable Note item component that displays note information in a card
 */
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: NoteListUiModel,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onNoteClick,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                if (note.content.isNotBlank()) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = note.updatedAtFormatted,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteItemPreview() {
    val sampleNote = Note(
        id = 1L,
        title = "Complete project proposal",
        content = "Finish writing the project proposal for the new mobile app",
        createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
        updatedAt = System.currentTimeMillis() - 3600000, // 1 hour ago
    )

    PreviewContainer {
        NoteItem(
            note = NoteUiMapper.toListUiModel(sampleNote),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemDarkPreview() {
    val sampleNote = Note(
        id = 1L,
        title = "Complete project proposal",
        content = "Finish writing the project proposal for the new mobile app",
        createdAt = System.currentTimeMillis() - 86400000,
        updatedAt = System.currentTimeMillis() - 3600000,
    )

    PreviewContainer(darkTheme = true) {
        NoteItem(
            note = NoteUiMapper.toListUiModel(sampleNote),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemEmptyContentPreview() {
    val sampleNote = Note(
        id = 2L,
        title = "Call dentist for appointment",
        content = "",
        createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
        updatedAt = System.currentTimeMillis() - 10800000, // 3 hours ago
    )

    PreviewContainer {
        NoteItem(
            note = NoteUiMapper.toListUiModel(sampleNote),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemLongContentPreview() {
    val sampleNote = Note(
        id = 3L,
        title = AppConstants.loremIpsum,
        content = AppConstants.loremIpsum,
        createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
        updatedAt = System.currentTimeMillis() - 7200000, // 2 hours ago
    )

    PreviewContainer {
        NoteItem(
            note = NoteUiMapper.toListUiModel(sampleNote),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}
