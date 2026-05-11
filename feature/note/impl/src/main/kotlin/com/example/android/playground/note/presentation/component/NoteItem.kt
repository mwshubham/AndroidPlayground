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
import com.example.android.playground.note.presentation.model.NoteListItemUiModel

/**
 * Reusable Note item component that displays note information in a card
 */
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: NoteListItemUiModel,
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
    PreviewContainer {
        NoteItem(
            note =
                NoteListItemUiModel(
                    id = 1L,
                    title = "Complete project proposal",
                    content = "Finish writing the project proposal for the new mobile app",
                    updatedAtFormatted = "1 hour ago",
                ),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteItem(
            note =
                NoteListItemUiModel(
                    id = 1L,
                    title = "Complete project proposal",
                    content = "Finish writing the project proposal for the new mobile app",
                    updatedAtFormatted = "1 hour ago",
                ),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemEmptyContentPreview() {
    PreviewContainer {
        NoteItem(
            note =
                NoteListItemUiModel(
                    id = 2L,
                    title = "Call dentist for appointment",
                    content = "",
                    updatedAtFormatted = "3 hours ago",
                ),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteItemLongContentPreview() {
    PreviewContainer {
        NoteItem(
            note =
                NoteListItemUiModel(
                    id = 3L,
                    title = AppConstants.loremIpsum,
                    content = AppConstants.loremIpsum,
                    updatedAtFormatted = "2 hours ago",
                ),
            onNoteClick = {},
            onDeleteClick = {},
        )
    }
}
