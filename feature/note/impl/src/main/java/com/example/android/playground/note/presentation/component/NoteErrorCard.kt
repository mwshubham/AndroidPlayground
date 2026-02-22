package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Reusable error card component for displaying error messages
 */
@Composable
fun NoteErrorCard(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onDismiss: () -> Unit,
    dismissText: String = "Tap to dismiss",
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        onClick = onDismiss,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = dismissText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
            )
        }
    }
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteErrorCardPreview() {
    PreviewContainer {
        NoteErrorCard(
            errorMessage = "Failed to load notes. Please check your internet connection.",
            onDismiss = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteErrorCardLongMessagePreview() {
    PreviewContainer {
        NoteErrorCard(
            errorMessage =
                """
                This is a very long error message that should wrap properly 
                and demonstrate how the error card handles longer text content.
                """.trimIndent(),
            onDismiss = {},
        )
    }
}

@ComponentPreview
@Composable
private fun NoteErrorCardDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteErrorCard(
            errorMessage = "Something went wrong while saving the note.",
            onDismiss = {},
        )
    }
}
