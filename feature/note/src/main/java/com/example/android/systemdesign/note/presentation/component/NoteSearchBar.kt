package com.example.android.systemdesign.note.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.systemdesign.core.ui.preview.ComponentPreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer

/**
 * Reusable search bar component for filtering notes
 */
@Composable
fun NoteSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search notes..."
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text(placeholder) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

// Preview functions using the new preview system
@ComponentPreview
@Composable
private fun NoteSearchBarEmptyPreview() {
    PreviewContainer {
        NoteSearchBar(
            searchQuery = "",
            onSearchQueryChange = {}
        )
    }
}

@ComponentPreview
@Composable
private fun NoteSearchBarWithTextPreview() {
    PreviewContainer {
        NoteSearchBar(
            searchQuery = "project",
            onSearchQueryChange = {}
        )
    }
}

@ComponentPreview
@Composable
private fun NoteSearchBarDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteSearchBar(
            searchQuery = "groceries",
            onSearchQueryChange = {}
        )
    }
}
