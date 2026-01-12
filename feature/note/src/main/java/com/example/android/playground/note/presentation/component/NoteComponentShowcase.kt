package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.common.util.DateFormatter
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.presentation.mapper.NoteUiMapper

/**
 * Showcase of all Note components for design system validation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteComponentShowcase(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Note Components Showcase",
                onNavigationClick = onNavigateBack,
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Search Bar Component
            Text("Search Bar Component:")
            NoteSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
            )

            // Error Card Component
            Text("Error Card Component:")
            NoteErrorCard(
                errorMessage = "Sample error message for demonstration",
                onDismiss = {},
            )

            // Note Item Component
            val sampleNote =
                Note(
                    id = 1L,
                    title = "Sample Note Title",
                    content = "This is a sample note content to demonstrate the component",
                    createdAt = System.currentTimeMillis() - 86400000,
                    updatedAt = System.currentTimeMillis() - 3600000,
                )

            Text("Note Item Component:")
            NoteItem(
                note = NoteUiMapper.toListUiModel(sampleNote),
                onNoteClick = {},
                onDeleteClick = {},
            )

            // Text Field Components
            Text("Text Field Components:")
            NoteTextField(
                value = "Sample Title",
                onValueChange = {},
                label = "Title",
            )

            NoteTextField(
                value = "Sample multiline content\nwith multiple lines\nfor demonstration",
                onValueChange = {},
                label = "Content",
                isMultiline = true,
                maxLines = 5,
            )

            // Metadata Card Component
            val createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
            val updatedAt = System.currentTimeMillis() - 7200000 // 2 hours ago

            Text("Metadata Card Component:")
            NoteMetadataCard(
                createdAtFormatted = DateFormatter.formatTimestamp(createdAt),
                updatedAtFormatted = DateFormatter.formatTimestamp(updatedAt),
            )
        }
    }
}

// Preview functions using the new preview system
@DualThemePreview
@Composable
private fun NoteComponentShowcasePreview() {
    PreviewContainer {
        NoteComponentShowcase()
    }
}

@FullPreview
@Composable
private fun NoteComponentShowcaseFullPreview() {
    PreviewContainer {
        NoteComponentShowcase()
    }
}
