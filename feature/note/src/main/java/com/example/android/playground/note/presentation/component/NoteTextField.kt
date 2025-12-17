package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Reusable text field component for note input with consistent styling
 */
@Composable
fun NoteTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1,
    isMultiline: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isMultiline) {
                    Modifier.height(200.dp)
                } else {
                    Modifier
                }
            )
            .padding(bottom = 16.dp),
        maxLines = if (isMultiline) maxLines else 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteTextFieldTitlePreview() {
    PreviewContainer {
        NoteTextField(
            value = "Sample note title",
            onValueChange = {},
            label = "Title"
        )
    }
}

@ComponentPreview
@Composable
private fun NoteTextFieldContentPreview() {
    PreviewContainer {
        NoteTextField(
            value = "This is a sample content for the note item that shows how multiline text input works.",
            onValueChange = {},
            label = "Content",
            isMultiline = true,
            maxLines = 10
        )
    }
}

@ComponentPreview
@Composable
private fun NoteTextFieldEmptyPreview() {
    PreviewContainer {
        NoteTextField(
            value = "",
            onValueChange = {},
            label = "Title"
        )
    }
}

@ComponentPreview
@Composable
private fun NoteTextFieldDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteTextField(
            value = "Dark theme title",
            onValueChange = {},
            label = "Title"
        )
    }
}

@ComponentPreview
@Composable
private fun NoteTextFieldMultilineDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteTextField(
            value = "This is multiline content in dark theme",
            onValueChange = {},
            label = "Content",
            isMultiline = true,
            maxLines = 10
        )
    }
}
