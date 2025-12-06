package com.example.android.systemdesign.note.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.systemdesign.core.ui.components.AppTopAppBar
import com.example.android.systemdesign.core.ui.preview.DualThemePreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer
import com.example.android.systemdesign.note.presentation.component.NoteErrorCard
import com.example.android.systemdesign.note.presentation.component.NoteMetadataCard
import com.example.android.systemdesign.note.presentation.component.NoteTextField
import com.example.android.systemdesign.note.presentation.viewmodel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        AppTopAppBar(
            title = if (state.note != null) "Note Details" else "New Note",
            onNavigationClick = onNavigateBack,
            actions = {
                IconButton(
                    onClick = {
                        if (state.isEditing) {
                            viewModel.saveNote()
                        } else {
                            viewModel.toggleEditing()
                        }
                    }
                ) {
                    Icon(
                        if (state.isEditing) Icons.Default.Save else Icons.Default.Edit,
                        contentDescription = if (state.isEditing) "Save" else "Edit"
                    )
                }
            }
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Error handling using extracted component
            state.error?.let { error ->
                NoteErrorCard(
                    errorMessage = error,
                    onDismiss = { /* Handle error dismissal if needed */ }
                )
            }

            // Loading indicator
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Title
                if (state.isEditing) {
                    NoteTextField(
                        value = state.title,
                        onValueChange = viewModel::updateTitle,
                        label = "Title"
                    )
                } else {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Content
                if (state.isEditing) {
                    NoteTextField(
                        value = state.content,
                        onValueChange = viewModel::updateContent,
                        label = "Content",
                        isMultiline = true,
                        maxLines = 10
                    )
                } else {
                    if (state.content.isNotBlank()) {
                        Text(
                            text = state.content,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Metadata using extracted component
                state.note?.let { note ->
                    if (!state.isEditing) {
                        NoteMetadataCard(
                            createdAt = note.createdAt,
                            updatedAt = note.updatedAt
                        )
                    }
                }

                // Cancel button when editing
                if (state.isEditing && state.note != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = viewModel::toggleEditing,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

// Preview functions using the new preview system
@DualThemePreview
@Composable
private fun NoteDetailScreenPreview() {
    PreviewContainer {
        NoteDetailScreenContent()
    }
}

@DualThemePreview
@Composable
private fun NoteDetailScreenDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteDetailScreenContent()
    }
}

@DualThemePreview
@Composable
private fun NoteDetailEditModePreview() {
    PreviewContainer {
        NoteDetailEditModeContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteDetailScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopAppBar(
            title = "Note Details",
            onNavigationClick = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Sample Note Title",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "This is sample content for the note item to demonstrate how it looks in the theme.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            NoteMetadataCard(
                createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                updatedAt = System.currentTimeMillis() - 3600000   // 1 hour ago
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteDetailEditModeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopAppBar(
            title = "Note Details",
            onNavigationClick = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Save, contentDescription = "Save")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            NoteTextField(
                value = "Sample Note Title",
                onValueChange = {},
                label = "Title"
            )

            NoteTextField(
                value = "This is sample content for editing mode",
                onValueChange = {},
                label = "Content",
                isMultiline = true,
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}
